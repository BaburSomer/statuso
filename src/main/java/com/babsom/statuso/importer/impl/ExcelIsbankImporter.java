package com.babsom.statuso.importer.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Component;

import com.babsom.statuso.importer.FileImporter;
import com.babsom.statuso.model.Transaction;
import com.babsom.statuso.model.TransactionDirection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("ExcelIsbankImporter")
public class ExcelIsbankImporter extends ExcelImporter implements FileImporter {

	private static final String SOURCE_EAB = "İşBankası - EAB";
	private static final String SOURCE_AYS = "İşBankası - Ayşe";
	private static final String SOURCE_BAB = "İşBankası - Babür (Davutpaşa)";

	@Override
	protected DateTimeFormatter obtainDateTimeFormatter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy-HH:mm:ss");
	}

	@Override
	public String obtainFileSource() {
		return "İŞBank Excel";
	}

	@Override
	protected void setSourceOfData(String name) {
		if (name.contains("246068")) {
			this.sourceOfData = SOURCE_EAB;
		} else if (name.contains("382420")) {
			this.sourceOfData = SOURCE_BAB;
		} else if (name.contains("250999")) {
			this.sourceOfData = SOURCE_AYS;
		} else {
			this.sourceOfData = SOURCE_NOT + name.substring(0, 20);
		}
	}

	@Override
	protected String obtainHeaderMarker() {
		return "Tarih/Saat";
	}

	@Override
	protected Transaction processLine(Row row) {
		log.debug(">>> - ExcelIsbankImporter-processLine");

		Timestamp timestamp = null;
		try {
			LocalDateTime localDateTime = LocalDateTime.parse(row.getCell(0).getStringCellValue(), dateTimeFormatter);
			if (localDateTime == null) {
				log.debug("<<< - ExcelIsbankImporter-processLine");
				return null;
			}
			timestamp = Timestamp.valueOf(localDateTime);
		} catch (Exception e) {
			log.debug("<<< - ExcelIsbankImporter-processLine");
			return null;
		} finally {

		}
		Transaction transaction = new Transaction();
		transaction.setMandt(1L);
		transaction.setTimestamp(timestamp);

		try {
			double     amount = row.getCell(3).getNumericCellValue();
			BigDecimal value  = new BigDecimal(amount);
			if (value.compareTo(BigDecimal.ZERO) == 0) {
				return null;
			} else {
				transaction.setDirection(value.compareTo(BigDecimal.ZERO) == -1 ? TransactionDirection.OUT : TransactionDirection.IN);
			}
			transaction.setAmount(value);
		} catch (Exception e) {
			log.debug("<<< - ExcelIsbankImporter-processLine");
			return null;
		} finally {

		}
		transaction.setSource(this.sourceOfData);
		transaction.setType(row.getCell(7).getStringCellValue());
		transaction.setDescription(row.getCell(8).getStringCellValue());
		transaction.setReference(row.getCell(14).getStringCellValue());

		log.debug("<<< - ExcelIsbankImporter-processLine");
		return transaction;
	}
}