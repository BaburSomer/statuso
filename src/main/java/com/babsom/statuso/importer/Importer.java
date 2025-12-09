package com.babsom.statuso.importer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.babsom.statuso.model.Transaction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Importer {

	public List<Transaction> importTransactions() throws IOException {
		log.debug(">>> - Processing transaction data files");
		String defPath     = System.getProperty("user.home");
		String rawDataPath = defPath + "\\" + "rawData";

		File rawDataFolder = new File(rawDataPath);
		if (rawDataFolder.exists()) {
			if (rawDataFolder.isDirectory()) {
				File[] files = rawDataFolder.listFiles((dir, name) -> !name.startsWith("~$")
						&& (name.toLowerCase().endsWith(".xls") || name.toLowerCase().endsWith(".xlsx") || name.toLowerCase().endsWith(".pdf")));
				for (File file : files) {
					importTransactionData(file);
				}
			} else {
				throw new FileNotFoundException("Target is not a directory " + rawDataPath);
			}
		} else {
			throw new FileNotFoundException("Could not find " + rawDataPath);
		}

		log.info("Searching for account transactions data files under " + rawDataPath);
		log.debug("<<< - Processing transaction data files");

		return new ArrayList<>();
	}

	private void importTransactionData(File file) throws FileNotFoundException, IOException {
		log.debug(">>> - Reading account transaction data of " + file.getName());

		try (FileInputStream fis = new FileInputStream(file)) {
			try (Workbook wb = WorkbookFactory.create(fis)) {
				if (wb.getNumberOfSheets() > 0) {
					Sheet sheet = wb.getSheetAt(0);
					handleSheet(sheet);
				}
			}
		}
		log.debug("<<< - Reading account transaction data of " + file.getName());
	}

	private void handleSheet(Sheet sheet) {
		for (int rowNum = sheet.getFirstRowNum(); rowNum < sheet.getLastRowNum(); rowNum++) {
			Row row = sheet.getRow(rowNum);
			if (row == null || row.getCell(0) == null) {
				continue;
			}
			Cell   cell    = row.getCell(0);
			String content = cell.getStringCellValue().trim();
			if (content.startsWith("Tarih")) {
				if (content.equalsIgnoreCase("Tarih/Saat")) {
					System.out.println("İŞ Bank");
				} else {
					System.out.println("Denizbank");
				}
			} else {
				continue;
			}
		}
	}
}