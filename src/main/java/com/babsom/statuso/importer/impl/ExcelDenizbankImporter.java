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
@Component("ExcelDenizbankImporter")
public class ExcelDenizbankImporter extends ExcelImporter implements FileImporter {
	private static final String SOURCE_LIF = "DenizBank - Elif";

	
	@Override
	protected DateTimeFormatter obtainDateTimeFormatter() {
		// 31.07.2025 02:59
		return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
	}

	@Override
	protected void setSourceOfData(String name) {
		this.sourceOfData = SOURCE_LIF;
//		if (name.contains("246068")) {
//			this.sourceOfData = SOURCE_EAB;
//		} else if (name.contains("382420")) {
//			this.sourceOfData = SOURCE_BAB;
//		} else if (name.contains("250999")) {
//			this.sourceOfData = SOURCE_AYS;
//		} else {
//			this.sourceOfData = SOURCE_NOT + name.substring(0, 20);
//		}
	}

	@Override
	public String obtainFileSource() {
		return "DenizBank Excel";
	}

	@Override
	protected String obtainHeaderMarker() {
		return "Tarih";
	}

	@Override
	protected Transaction processLine(Row row) {
		log.debug(">>> - ExcelDenizbankImporter-processLine");

		Timestamp timestamp = null;
		try {
			LocalDateTime localDateTime = LocalDateTime.parse(row.getCell(0).getStringCellValue(), dateTimeFormatter);
			if (localDateTime == null) {
				return null;
			}
			timestamp = Timestamp.valueOf(localDateTime);
		} catch (Exception e) {
			return null;
		}

		Transaction transaction = new Transaction();
		transaction.setMandt(1L);
		transaction.setTimestamp(timestamp);

		try {
			double     amount = row.getCell(5).getNumericCellValue();
			BigDecimal value  = new BigDecimal(amount);
			if (value.compareTo(BigDecimal.ZERO) == 0) {
				return null;
			} else {
				transaction.setDirection(value.compareTo(BigDecimal.ZERO) == -1 ? TransactionDirection.OUT : TransactionDirection.IN);
			}
			transaction.setAmount(value);
		} catch (Exception e) {
			return null;
		}
		transaction.setSource(this.sourceOfData);
		transaction.setType(row.getCell(1).getStringCellValue());
		transaction.setDescription(row.getCell(2).getStringCellValue());
		transaction.setReference(row.getCell(3).getStringCellValue());
		log.debug("<<< - ExcelDenizbankImporter-processLine");
		return transaction;
	}
}