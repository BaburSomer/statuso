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
		if (excelFactory.supports(file)) {
			return excelFactory.createImporter(file);
		}

		if (pdfFactory.supports(file)) {
			return pdfFactory.createImporter(file);
		}
		throw new IllegalArgumentException("Unsupported file type: " + file.getFileName());
	}
}