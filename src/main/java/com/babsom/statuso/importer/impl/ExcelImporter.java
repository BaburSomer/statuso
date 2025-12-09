package com.babsom.statuso.importer.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.babsom.statuso.importer.FileImporter;
import com.babsom.statuso.model.Transaction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ExcelImporter implements FileImporter {

	protected final DateTimeFormatter dateTimeFormatter;

	protected String sourceOfData;

	public ExcelImporter() {
		super();
		this.dateTimeFormatter = this.obtainDateTimeFormatter();
	}

	abstract protected DateTimeFormatter obtainDateTimeFormatter();

	abstract protected Transaction processLine(Row row);

	abstract protected String obtainHeaderMarker();

	protected void setSourceOfData(String name) {
		this.sourceOfData = SOURCE_NOT + name.substring(0, 20);
	}

	@Override
	public List<Transaction> importData(Path path) {
		log.debug(">>> - ExcelImporter-importData");
		log.debug("📊 Parsing " + obtainFileSource() + ": " + path);

		ArrayList<Transaction> transactions = new ArrayList<>();
		try (FileInputStream fis = new FileInputStream(path.toFile())) {
			this.setSourceOfData(path.toFile().getName());
			try (Workbook wb = WorkbookFactory.create(fis)) {
				if (wb.getNumberOfSheets() > 0) {
					transactions = handleSheet(wb.getSheetAt(0));
				}
			}
		} catch (FileNotFoundException e) {
			log.error("❌ Cannot find file - " + path);
		} catch (IOException e) {
			log.error("❌ Error while processing - " + path + " ==> " + e.getLocalizedMessage());
		} finally {
			log.debug("<<< - ExcelImporter-importData");
		}
		return transactions;
	}

	private ArrayList<Transaction> handleSheet(Sheet sheet) {
		log.debug(">>> - ExcelIsbankImporter-handleSheet");
		ArrayList<Transaction> transactions = new ArrayList<>();

		boolean processLine = false;
		for (int rowNum = sheet.getFirstRowNum(); rowNum <= sheet.getLastRowNum(); rowNum++) {
			Row row = sheet.getRow(rowNum);
			if (row == null || row.getCell(0) == null) {
				continue;
			} else {
				if (!processLine) {
					if (row.getCell(0).getStringCellValue().equals(obtainHeaderMarker())) {
						processLine = true;
					}
					continue;
				}
			}
			Transaction transaction = this.processLine(row);
			if (transaction != null) {
				transactions.add(transaction);
			}
		}

		log.debug("<<< - ExcelIsbankImporter-handleSheet");
		return transactions;
	}
}