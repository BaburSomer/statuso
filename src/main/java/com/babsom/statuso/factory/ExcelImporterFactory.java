package com.babsom.statuso.factory;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.babsom.statuso.importer.FileImporter;
import com.babsom.statuso.importer.ImporterProviderFactory;
import com.babsom.statuso.importer.impl.ExcelDenizbankImporter;
import com.babsom.statuso.importer.impl.ExcelIsbankImporter;

@Component
public class ExcelImporterFactory implements ImporterProviderFactory {

	@Override
	public boolean supports(Path file) {
		String name = file.getFileName().toString().toLowerCase();
		return name.endsWith(".xls") || name.endsWith(".xlsx");
	}

	@Override
	public FileImporter createImporter(Path file) {
		String fileName = file.getFileName().toString();

	    if (fileName.contains("HesapOzeti")) return new ExcelIsbankImporter();
       if (fileName.contains("Hareketleri")) return new ExcelDenizbankImporter();

		throw new IllegalArgumentException("No Excel importer matches file: " + fileName);
	}
}
