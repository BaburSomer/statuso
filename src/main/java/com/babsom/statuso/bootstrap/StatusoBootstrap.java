package com.babsom.statuso.bootstrap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StatusoBootstrap implements ApplicationListener<ContextRefreshedEvent>{

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.debug(">>> - Bootstrap");
		try {
			this.readAccountTransactions();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
		log.debug("<<< - Bootstrap");
	}

	private void readAccountTransactions() throws IOException {
		log.debug(">>> - Processing transaction data files");
		String defPath = System.getProperty("user.home");
		String rawDataPath = defPath + "\\" + "rawData";
		String processedDataPath = rawDataPath + "\\" + "processed";
		
		File rawDataFolder = new File(rawDataPath);
		if (rawDataFolder.exists()) {
			if (rawDataFolder.isDirectory()) {
				File[] files = rawDataFolder
						.listFiles((dir, name) -> !name.startsWith("~$") && (name.toLowerCase().endsWith(".xls") || name.toLowerCase().endsWith(".xlsx")));
//				FilenameFilter filter = new FilenameFilter() {
//					
//					@Override
//					public boolean accept(File dir, String name) {
//						return name.toLowerCase().endsWith(".xls") || name.toLowerCase().endsWith(".xlsx");
//					}
//				};
				for (File file : files) {
					importTransactionData(file);
				}
			} else {
				throw new FileNotFoundException("Target is not a directory " + rawDataPath);
			}
		}		else {
			throw new FileNotFoundException("Could not find " + rawDataPath);
		}
		
		log.info("Searching for account transactions data files under " + rawDataPath);
		log.debug("<<< - Processing transaction data files");
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
			if(row == null || row.getCell(0) == null) {
				continue;
			}
			System.out.println("Row " + rowNum + " - " + row.getCell(0));
		}
	}
}