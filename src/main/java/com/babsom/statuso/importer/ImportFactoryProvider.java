package com.babsom.statuso.importer;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ImportFactoryProvider {
	private final Map<String, ImporterProviderFactory> factories;

   public ImportFactoryProvider(Map<String, ImporterProviderFactory> factories) {
       this.factories = factories;
   }

   public ImporterProviderFactory getFactory(String filePath) {
   	filePath = filePath.toLowerCase();
       if (filePath.endsWith(".xls") || filePath.endsWith(".xlsx")) {
           return factories.get("excel");
       } else if (filePath.endsWith(".pdf")) {
           return factories.get("pdf");
       }
       throw new IllegalArgumentException("Unsupported file type: " + filePath);
   }
}