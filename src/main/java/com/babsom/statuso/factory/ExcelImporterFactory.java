package com.babsom.statuso.factory;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.babsom.statuso.importer.FileImporter;
import com.babsom.statuso.importer.ImporterProviderFactory;
import com.babsom.statuso.model.FactoryCreationRule;
import com.babsom.statuso.repository.FactoryCreationRuleRepository;

@Component
public class ExcelImporterFactory implements ImporterProviderFactory {
	private static final String FACTORY_CLASSES_PATH = "com.babsom.statuso.importer.impl.";
	
	private final FactoryCreationRuleRepository ruleRepository;
	
	public ExcelImporterFactory(FactoryCreationRuleRepository ruleRepository) {
		super();
		this.ruleRepository = ruleRepository;
	}

	@Override
	public boolean supports(Path file) {
		String name = file.getFileName().toString().toLowerCase();
		return name.endsWith(".xls") || name.endsWith(".xlsx");
	}

	@Override
	public FileImporter createImporter(Path file) {
		String fileName = file.getFileName().toString();
		Iterable<FactoryCreationRule> rules = this.ruleRepository.findAll();
		
		for (FactoryCreationRule rule : rules) {
			if (fileName.contains(rule.getRule())) {
				return createReflectedFileImporter(rule);
			}
		}

		throw new IllegalArgumentException("No Excel importer matches file: " + fileName);
	}

	private FileImporter createReflectedFileImporter(FactoryCreationRule rule) {
		try {
         Class<?> clazz = Class.forName(FACTORY_CLASSES_PATH + rule.getImporterClass());
         Object instance = clazz.getDeclaredConstructor().newInstance();

         if (!(instance instanceof FileImporter)) {
             throw new IllegalArgumentException("Class " + rule.getImporterClass() + " is not an Importer");
         }

         return (FileImporter) instance;
     } catch (Exception e) {
         throw new RuntimeException("Failed to instantiate importer for provider " + rule.getName(), e);
     }
	}
}
