package com.babsom.statuso.importer;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.babsom.statuso.factory.ExcelImporterFactory;
import com.babsom.statuso.factory.PdfImporterFactory;

@Component
public class MainImporterFactory {

	private final ExcelImporterFactory excelFactory;
	private final PdfImporterFactory   pdfFactory;

	public MainImporterFactory(ExcelImporterFactory excelFactory, PdfImporterFactory pdfFactory) {
		this.excelFactory = excelFactory;
		this.pdfFactory   = pdfFactory;
	}

	public FileImporter createImporter(Path file) {
		String fileName = file.getFileName().toString().toLowerCase();
		if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
			return excelFactory.createImporter(file);
		} else if (fileName.endsWith(".pdf")) {
			return pdfFactory.createImporter(file);
		} else {
			throw new IllegalArgumentException("Unsupported file type: " + fileName);
		}
	}
}
