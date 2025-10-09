package com.babsom.statuso.importer.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Configuration
@ConfigurationProperties(prefix = "import")
@Getter
@Setter
@ToString
public class ImporterProperties {

   private String baseDir;
   private String processedDir;
   private String errorDir;

	private List<ImportFactoryConfig> factories;

}