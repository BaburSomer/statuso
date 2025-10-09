package com.babsom.statuso.importer.config;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ImportFactoryConfig {

	private String                      type;    // excel or pdf
	private List<ProviderPatternConfig> patterns;
}
