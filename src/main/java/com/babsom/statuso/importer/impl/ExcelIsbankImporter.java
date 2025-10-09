package com.babsom.statuso.importer.impl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.babsom.statuso.importer.FileImporter;
import com.babsom.statuso.model.Transaction;

@Component("ExcelIsbankImporter")
public class ExcelIsbankImporter implements FileImporter {

	@Override
	public List<Transaction> importData(Path file) {
		System.out.println("📊 Parsing İŞBank Excel: " + file);
		// TODO: implement actual Excel parsing
		return new ArrayList<>();
	}
}