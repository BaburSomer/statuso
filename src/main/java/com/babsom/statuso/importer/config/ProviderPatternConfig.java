package com.babsom.statuso.importer.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProviderPatternConfig {

	private String provider;
	private String pattern;
	private String importer;
}
