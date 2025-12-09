package com.babsom.statuso.importer.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.babsom.statuso.importer.FileImporter;
import com.babsom.statuso.model.Transaction;
import com.babsom.statuso.model.TransactionDirection;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("PdfIsbankImporter")
public class PdfIsbankImporter extends PdfImporter implements FileImporter {

	private static final Pattern DATE_PATTERN        = Pattern.compile("^\\s*(\\d{2}/\\d{2}/\\d{4})(?!\\d)(.*)$");
	private static final Pattern AMOUNT_PATTERN      = Pattern.compile("-?\\d{1,3}(?:\\.\\d{3})*,\\d{2}|-?\\d+", Pattern.UNICODE_CHARACTER_CLASS);
	private static final String  SOURCE_EAB          = "KrediKartı";
	private static final Pattern DESCRIPTION_PATTERN = Pattern
			.compile("^(.*?)\\s*(-?\\d{1,3}(?:\\.\\d{3})*,\\d+|-?\\d{1,3}(?:\\.\\d{3})*|-?\\d+)\\s*([A-Z]{3})(?:\\b.*)?$");

	@Override
	protected DateTimeFormatter obtainDateTimeFormatter() {
		return DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm.ss");
	}

	@Override
	protected Pattern obtainTransactionPattern() {
		return Pattern.compile("^\\d{2}/\\d{2}/\\d{4}.*");
	}

	@Override
	public String obtainFileSource() {
		return "İŞBank KrediKart";
	}

	@Override
	protected String normalizeLines(String text) {
		text = text.replaceFirst("(?s)^.*?(\\d{2}/\\d{2}/\\d{4})", "$1"); // Remove everything before first date
		text = text.replaceAll("(\\d{2}/\\d{2}/\\d{4})(?=\\S)", "$1 ");   // Ensure space after each date
		text = text.replaceAll("\\r\\n?", "\n");
//		text = text.replaceAll("\\n(?!(\\d{2}/\\d{2}/\\d{4}))", " ");
		text = text.replaceAll("KAZANILAN MAXIPUAN:[^\\s]+", "");
		text = text.replaceAll("\\b\\d{1,2}/\\d{1,2}\\s+taksidi\\s+\\(\\d{1,3}(?:\\.\\d{3})*,\\d{2}\\)", "");
		return text;
	}

	@Override
	protected Transaction processLine(String line) {
		log.debug("📢 >>> - PdfIsbankImporter-processLine");
		int          errorPoint  = -1;
		final String lineToParse = line;
		try {
			line = line.strip();

			if (line.isEmpty() || !DATE_PATTERN.matcher(line).find()) {
				return null;
			}

			this.increaseNumOfTransactions();

			line = line.replaceAll(
					"(?s)(SANAL KART NO:|KRED[Iİ] KARTI HESAP ÖZET[İI]|\\*{3}\\s*[ÖO]DEMELER[İI]N[İI]Z [İI]Ç[İI]N TESEKK[ÜU]R EDER[İI]Z).*", "").trim();

			List<String> endMarkers = List.of("*** ÖDEMELERINIZIÇIN TESEKKÜR EDERIZ***", "HCE KART NO:", "SANALKART NO:");

			for (String endMarker : endMarkers) {
				int index = line.indexOf(endMarker);
				if (index > 0) {
					line = line.substring(0, index);
					break; // stop at the first match
				}
			}

			Matcher      amountMatcher = AMOUNT_PATTERN.matcher(line);
			List<String> nums          = new ArrayList<>();

			while (amountMatcher.find()) {
				nums.add(amountMatcher.group());
			}

			if (nums.isEmpty()) {
				errorPoint = 1;
				return null;
			}

			Timestamp timestamp = null;
			try {
				String dateStr = line.substring(0, 10) + " 00:00.00";

				LocalDateTime localDateTime = LocalDateTime.parse(dateStr, dateTimeFormatter);
				if (localDateTime == null) {
					errorPoint = 2;
					return null;
				}
				timestamp = Timestamp.valueOf(localDateTime);
			} catch (Exception e) {
				errorPoint = 3;
				return null;
			}

			Transaction transaction = new Transaction();
			transaction.setMandt(1L);
			transaction.setTimestamp(timestamp);
			transaction.setType(SOURCE_EAB);
			transaction.setReference(line);

			BigDecimal lastNum     = parseAmount(nums.get(nums.size() - 1));
			int        amountIndex = -1;
			if (lastNum.compareTo(new BigDecimal("1")) < 0 && nums.size() > 1 && !line.contains("OTOMATİK TAHSİLAT")) {
				lastNum     = parseAmount(nums.get(nums.size() - 2));
				amountIndex = line.indexOf(String.format(obtainLocale(), "%,d", lastNum.intValue()));
			} else {
				amountIndex = line.indexOf(String.format(obtainLocale(), "%,d", lastNum.intValue()));
				BigDecimal penultimateNum = parseAmount(nums.get(nums.size() - 2)); // vorletzte, sondan bir önceki
				BigDecimal year           = parseAmount(nums.get(2));

				if (amountIndex < 11 || (penultimateNum.compareTo(lastNum) > 0 && penultimateNum.compareTo(year) != 0)
						&& !line.contains("OTOMATİK TAHSİLAT") && !line.contains("HGS YÜKL.") && !line.contains("HESAPTAN AKTARIM")) {
					lastNum     = parseAmount(nums.get(nums.size() - 2));
					amountIndex = line.indexOf(String.format(obtainLocale(), "%,d", lastNum.intValue()));
				}
			}

			transaction.setAmount(lastNum);
			if (amountIndex >= 11) {
				String description = line.substring(11, amountIndex).trim();
				if (description == null) {
					errorPoint = 4;
					return null;
				}
				if (description.endsWith("-")) {
					transaction.setAmount(transaction.getAmount().negate());
					int idx = description.lastIndexOf(" ");
					description = description.substring(0, idx);
				}

				if (line.contains("SATIŞ") || line.contains("EUR") || line.contains("USD")) {
					Matcher m = DESCRIPTION_PATTERN.matcher(line.trim());
					if (m.find()) {
//						description = m.group(1).strip();
						transaction.setOriginalAmount(parseAmount(m.group(2).strip()));
						transaction.setCurrency(m.group(3));
						transaction.setExchangeRate(transaction.getAmount().abs().divide(transaction.getOriginalAmount(), 2, RoundingMode.HALF_UP));
					}
				} else {
					log.debug("📢 Could not split properly: " + line);
				}
				transaction.setDescription(description);
			}
			transaction.setDirection(transaction.getAmount().compareTo(BigDecimal.ZERO) == -1 ? TransactionDirection.IN : TransactionDirection.OUT);

			if (transaction.getDescription() == null || transaction.getDescription().contains("MAXİPUAN İLAVE:")) {
				log.debug("📢 Unneccessary transaction line: " + line);
				return null;
			}

			transaction.setDescription(truncateUtf8(transaction.getDescription(), 255));
			transaction.setReference(truncateUtf8(transaction.getReference(), 255));

			return transaction;
		} catch (Throwable t) {
			errorPoint = 5;
			return null;
		} finally {
			logEventuallyErrors(errorPoint, lineToParse);
			log.debug("📢 <<< - PdfIsbankImporter-processLine");
		}
	}

	private void logEventuallyErrors(int errorPoint, String lineToParse) {
		switch (errorPoint) {
			case 1: {
				log.warn("⚠️ No numerical values ​​could be detected. Parsed line <<< " + lineToParse + " >>>");
				break;
			}
			case 2: {
				log.warn("⚠️ No date value could be found. Parsed line <<< " + lineToParse + " >>>");
				break;
			}
			case 3: {
				log.warn("⚠️ Error while parsing date value. Parsed line <<< " + lineToParse + " >>>");
				break;
			}
			case 4: {
				log.warn("⚠️ Description could not be parsed out. Parsed line <<< " + lineToParse + " >>>");
				break;
			}
			case 5: {
				log.warn("⚠️ Something went terribly wrong. Parsed line <<< " + lineToParse + " >>>");
				break;
			}
			default:
				break;
		}

		if (errorPoint > 0) {
			increaseNumOfErrors();
		}
	}

	public static String truncateUtf8(String input, int maxBytes) {
		if (Objects.isNull(input)) {
			return null;
		}

		byte[] utf8 = input.getBytes(StandardCharsets.UTF_8);
		if (utf8.length <= maxBytes) {
			return input;
		}

		// Cut bytes safely without breaking UTF-8 sequence
		int end = maxBytes;
		while (end > 0 && (utf8[end] & 0b11000000) == 0b10000000) {
			// Skip continuation bytes (10xxxxxx)
			end--;
		}

		return new String(utf8, 0, end, StandardCharsets.UTF_8);
	}
}