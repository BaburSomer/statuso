package com.babsom.statuso.importer;

import java.nio.file.Path;

public interface ImporterProviderFactory {

	boolean supports(Path file);

	FileImporter createImporter(Path file);
}