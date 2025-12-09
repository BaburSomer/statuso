package com.babsom.statuso.importer.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.babsom.statuso.importer.FileImporter;
import com.babsom.statuso.model.Transaction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class PdfImporter implements FileImporter {

	protected final DateTimeFormatter dateTimeFormatter;
	protected final Pattern           transactionPattern;
	protected String                  sourceOfData;
	protected int                     numOfTransactions;
	protected int                     numOfErrors;

	public PdfImporter() {
		super();
		this.dateTimeFormatter  = this.obtainDateTimeFormatter();
		this.transactionPattern = this.obtainTransactionPattern();
	}

	protected void setSourceOfData(String name) {
		this.sourceOfData = SOURCE_NOT + name.substring(0, 20);
	}

	protected String obtainMonthYear(Timestamp ts) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(ts.getTime());

		String displayMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, obtainLocale());
		int    currentYear  = calendar.get(Calendar.YEAR);
		int    lastTwo      = currentYear % 100;
		String year         = (lastTwo < 10) ? "0" + lastTwo : String.valueOf(lastTwo);

		return displayMonth.toUpperCase() + year;
	}

	abstract protected DateTimeFormatter obtainDateTimeFormatter();

	abstract protected Pattern obtainTransactionPattern();

	abstract protected Transaction processLine(String line);

	abstract protected String normalizeLines(String line);

	@Override
	public List<Transaction> importData(Path path) {

		log.debug("📢 >>> - PdfImporter-importData");
		List<Transaction> transactions = new ArrayList<>();

		this.numOfTransactions = 0;
		this.numOfErrors       = 0;

		try {
			log.info("📊 Parsing " + obtainFileSource() + ": " + path);

			try (PDDocument document = PDDocument.load(path.toFile())) {
				this.setSourceOfData(path.toFile().getName());

				String lines = normalizeLines((new PDFTextStripper()).getText(document));

				for (String line : lines.split("\\R")) {
					Transaction transaction = processLine(line);
					if (transaction != null) {
						transactions.add(transaction);
					}
				}
				String startDateTag = obtainMonthYear(transactions.get(1).getTimestamp());
				String endDateTag   = obtainMonthYear(transactions.get(transactions.size() - 1).getTimestamp());
				String period       = startDateTag + " - " + endDateTag;
				transactions.forEach(t -> t.setSource(period));
			} catch (IOException e) {
				log.error("❌ Error while processing - " + path + " ==> " + e.getLocalizedMessage());
			}
		} finally {
			log.info("💬 " + this.numOfTransactions + " transactions proccessed in " + path.toFile().getName() + " with " + this.numOfErrors + " errors.");
			log.debug("📢 <<< - PdfImporter-importData");
		}
		return transactions;
	}

	protected BigDecimal parseAmount(String str) {
		String normalized = str.replace(".", "").replace(",", ".");
		return new BigDecimal(normalized);
	}
	
	protected void increaseNumOfTransactions() {
		this.numOfTransactions++;
	}
	
	protected void increaseNumOfErrors() {
		this.numOfErrors++;
	}
}