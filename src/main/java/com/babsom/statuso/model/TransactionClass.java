package com.babsom.statuso.model;

public enum TransactionClass {

	ACCOUNTANT("Muhasebeci"), AFRODIT("Afrodit'teki Masraflar"), BANK_EXPENSE("Banka Masrafları"), BANK_TRANSFER("Hesaptan hesaba para transferi"),
	CAR("Araba Masrafları"), CHILDREN("Çocuklara Masraflar"), CLEANING("Temizlik Masrafları"), CLOTHING("Kılık-Kıyafet"), CREDIT("Kredi Ödemesi"),
	CREDIT_CARD("Kredikartı Ödemesi"), CURRENCY_PURCHASE("Döviz Alım"), DATCA("Datça Masrafları"), DRINKING("İçki"),
	ELECTRICITY("Elektrik Masrafları"), ENTERTAINMENT("Eğlence / Kültür"), FINES("Cezalar"), FOREIGN_SALE("Döviz Satış"), GAS("Doğalgaz Masrafları"),
	HEALTH("Sağlık Masrafları"), HOUSEHOLD("Gündelik Masraflar"), INSURRANCE("Sigortalar"), INTEREST("Faiz gelirleri"),
	INVESTMENT("Yatırım Masrafları"), LEGAL_EXPENSE("Hukuki Masraflar"), MISC("Envai Çeşit Masraflar"), OTHER("Diğer"), PARENTS("Ebeveyn Masrafları"),
	PENSION("Emeklilik maaşı"), PERSONAL_CARE("Kişisel Bakım"), RENT("Kira Geliri"), RESTAURANT("Lokanta / Bar"), SALARY("Çalışan maaşı"),
	SHOPPING("Alışveriş"), TAX("Vergiler"), TELEPHONE("Telefon Masrafları"), TRANSPORTATION("Yolculuk Masrafları"), UNDEFINED("Tanımsız Masraflar"),
	VACATION("Gezi / Tatil"), WATER("Su Masrafları"), WEDDING("Düğün Masrafları"), WITHDRAWAL("Para çekme");

	private final String description;

	TransactionClass(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
}
