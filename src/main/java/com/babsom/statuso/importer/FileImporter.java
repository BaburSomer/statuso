package com.babsom.statuso.importer;

import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

import com.babsom.statuso.model.Transaction;

public interface FileImporter {

	static final String SOURCE_NOT = "Unknown - ";

	List<Transaction> importData(Path path);

	String obtainFileSource();
	
	default Locale obtainLocale() {
		return Locale.of("tr", "TR");
	}
}