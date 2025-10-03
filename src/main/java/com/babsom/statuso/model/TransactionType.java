package com.babsom.statuso.model;

public enum TransactionType {

	DEPOSIT(1, "Para yatırma"), OTHER(2, "Diğer"), PAYMENT(3, "Ödemeler"), POS(4, "POS ile ödeme"), TRANSFER(5, "Havale/EFT/FAST"),
	WITHDRAWAL(6, "Para çekme");

	private final String description;
	private final long   oid;

	TransactionType(int oid, String description) {
		this.oid         = oid;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public long getOid() {
		return oid;
	}
}