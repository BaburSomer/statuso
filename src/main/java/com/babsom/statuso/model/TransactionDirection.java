package com.babsom.statuso.model;

public enum TransactionDirection {

	IN("Girdi"), OUT("Çıktı");

	private final String description;

	TransactionDirection(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}