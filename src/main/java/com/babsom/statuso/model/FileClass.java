package com.babsom.statuso.model;

public enum FileClass {

	EXCEL("Excel"), PDF("PDF");

	private final String description;

	FileClass( String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public FileClass fetchFileClassByName(String name) {
		return name.equals(EXCEL.name()) ? EXCEL : PDF;
	}
	
	public FileClass fetchFileClassByDescription(String description) {
		return description.equalsIgnoreCase(EXCEL.getDescription()) ? EXCEL : PDF;
	}
}