package com.babsom.statuso.model;

public enum IncomeType {

	FOREIGN_CURRENCY(1, "Döviz Satış"), INTEREST(2, "Faiz gelirleri"), OTHER(3, "Diğer"), PENSION(4, "Emeklilik maaşı"), RENT(5, "Kira Geliri"), SALARY(6, "Çalışan maaşı"), ;

	private final String description;
	private final long   oid;

	IncomeType(int oid, String description) {
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