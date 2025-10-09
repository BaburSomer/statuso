package com.babsom.statuso.service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;

import com.babsom.statuso.importer.FileImporter;
import com.babsom.statuso.importer.MainImporterFactory;
import com.babsom.statuso.importer.config.ImporterProperties;
import com.babsom.statuso.model.Transaction;

import jakarta.annotation.PostConstruct;

@Service
public class ImportService {

	private final MainImporterFactory importFactory;
	private final ImporterProperties  props;

	public ImportService(MainImporterFactory importFactory, ImporterProperties props) {
		this.importFactory = importFactory;
		this.props         = props;
	}

	/** Run both existing file processing and watcher setup after startup */
	@PostConstruct
	public void init() {
		processExistingFiles();
		startDirectoryWatcher();
	}

	private void processExistingFiles() {
		Path importDir = Paths.get(props.getBaseDir());
		if (!Files.exists(importDir))
			return;

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(importDir)) {
			for (Path file : stream) {
				if (Files.isRegularFile(file)) {
					processFile(file);
				}
			}
		} catch (IOException e) {
			System.err.println("⚠️ Could not process existing files: " + e.getMessage());
		}
	}

	private void startDirectoryWatcher() {
		Executors.newSingleThreadExecutor().submit(() -> {
			try {
				Path dir = Paths.get(props.getBaseDir());
				if (!Files.exists(dir))
					Files.createDirectories(dir);

				WatchService watcher = FileSystems.getDefault().newWatchService();
				dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
				System.out.println("👀 Watching directory: " + dir);

				while (true) {
					WatchKey key = watcher.take();
					for (WatchEvent<?> event : key.pollEvents()) {
						Path fileName = (Path) event.context();
						Path fullPath = dir.resolve(fileName);
						// Wait a bit to ensure file is fully written
						Thread.sleep(500);
						if (Files.isRegularFile(fullPath)) {
							processFile(fullPath);
						}
					}
					key.reset();
				}
			} catch (Exception e) {
				System.err.println("❌ Directory watcher error: " + e.getMessage());
			}
		});
	}

	private void processFile(Path file) {
		System.out.println("📥 Processing: " + file.getFileName());
		try {
			FileImporter      importer     = importFactory.createImporter(file);
			List<Transaction> transactions = importer.importData(file);
			System.out.printf("✅ Imported %d transactions from %s%n", transactions.size(), file.getFileName());
			moveFile(file, props.getProcessedDir());
		} catch (Exception ex) {
			System.err.printf("❌ Failed to import %s: %s%n", file.getFileName(), ex.getMessage());
			moveFile(file, props.getErrorDir());
		}
	}

	private void moveFile(Path file, String targetDir) {
		try {
			Path targetPath = Paths.get(targetDir);
			if (!Files.exists(targetPath))
				Files.createDirectories(targetPath);
			Files.move(file, targetPath.resolve(file.getFileName()), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ex) {
			System.err.println("⚠️ Could not move file: " + ex.getMessage());
		}
	}
}
