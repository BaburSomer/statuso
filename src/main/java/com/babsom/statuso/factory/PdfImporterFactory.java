package com.babsom.statuso.factory;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.babsom.statuso.importer.FileImporter;
import com.babsom.statuso.importer.ImporterProviderFactory;
import com.babsom.statuso.importer.impl.PdfIsbankImporter;

@Component
public class PdfImporterFactory implements ImporterProviderFactory {

	@Override
	public boolean supports(Path file) {
		String name = file.getFileName().toString().toLowerCase();
		return name.endsWith(".pdf");
	}

	@Override
	public FileImporter createImporter(Path file) {
		String fileName = file.getFileName().toString();

		if (fileName.contains("Kredi Kartı Hesap Özeti")) return new PdfIsbankImporter();
		
		
		throw new IllegalArgumentException("No PDF importer matches file: " + fileName);
	}
}
