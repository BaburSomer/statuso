package com.babsom.statuso.model;

public enum ExpenditureType {

	ACCOUNTANT(1, "Muhasebeci"), AFRODIT(2, "Afrodit'teki Masraflar"), BANK_EXPENSE(3, "Banka Masrafları"), BANK_TRANSFER(4, "Hesaptan hesaba para transferi"),
	CAR(5, "Araba Masrafları"), CHILDREN(6, "Çocuklara Masraflar"), CLEANING(7, "Temizlik Masrafları"), CLOTHING(8, "Kılık-Kıyafet"), CREDIT(9, "Kredi Ödemesi"),
	CREDIT_CARD(10, "Kredikartı Ödemesi"), DATCA(11, "Datça Masrafları"), DRINKING(12, "İçki"), ELECTRICITY(13, "Elektrik Masrafları"),
	ENTERTAINMENT(14, "Eğlence / Kültür"), FINES(15, "Cezalar"), FOREIGN_CURRENCY(16, "Döviz Alım"), GAS(17, "Doğalgaz Masrafları"),
	HEALTH(18, "Sağlık Masrafları"), HOUSEHOLD(19, "Gündelik Ev Masrafları"), INSURRANCE(20, "Sigortalar"), INVESTMENT(21, "Yatırım Masrafları"),
	LEGAL_EXPENSE(22, "Hukuki Masraflar"), MISC(23, "Envai Çeşit Masraflar"), PARENTS(24, "Ebeveyne Masrafları"), PERSONAL_CARE(25, "Kişisel Bakım"),
	RESTAURANT(26, "Lokanta / Bar"), SHOPPING(27, "Alışveriş"), TAX(28, "Vergiler"), TELEPHONE(29, "Telefon Masrafları"),
	TRANSPORTATION(30, "Yolculuk Masrafları"), UNDEFINED(31, "Tanımsız Masraflar"), VACATION(32, "Gezi / Tatil"), WATER(33, "Su Masrafları"), WEDDING(34, "Düğün Masrafları"), ;

	private final String description;
	private final long   oid;

	ExpenditureType(int oid, String description) {
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
