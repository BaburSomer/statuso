package com.babsom.statuso.importer;

import java.nio.file.Path;
import java.util.List;

import com.babsom.statuso.model.Transaction;

public interface FileImporter {

	List<Transaction> importData(Path path);
}