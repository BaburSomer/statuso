package com.babsom.statuso.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.babsom.statuso.model.CurrencyRate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ExchangeRatesFetchService {

	private final WebClient           webClient;
	private final ObjectMapper        objectMapper = new ObjectMapper();
	private final String              apiKey;
	private final CurrencyRateService rateService;

	public ExchangeRatesFetchService(CurrencyRateService rateService, @Value("${exchange.api.base-url}") String baseUrl,
			@Value("${exchange.api.key}") String apiKey) {
		this.webClient   = WebClient.builder().baseUrl(baseUrl).build();
		this.apiKey      = apiKey;
		this.rateService = rateService;
	}

	/**
	 * Fetches historical exchange rates for multiple target currencies.
	 *
	 * @param baseCurrency     base currency (e.g. "TRY")
	 * @param year             year (e.g. 2025)
	 * @param month            month (e.g. 10)
	 * @param day              day (e.g. 6)
	 * @param targetCurrencies one or more target currency codes (e.g. "USD", "EUR",
	 *                         "GBP")
	 * @return Mono<Map<String, Double>> where key = currency code, value = rate
	 */
	public Mono<CurrencyRate> getOrFetchExchangeRate(String baseCurrency, int year, int month, int day, String... targetCurrencies) {
		
	   LocalDate date = LocalDate.of(year, month, day);
		String key = date.format(DateTimeFormatter.ISO_LOCAL_DATE);
		CurrencyRate existing = rateService.getRate(key);
		if (existing != null) {
         return Mono.just(existing);
		}
		
		String uri = String.format("/v6/%s/history/%s/%d/%d/%d", apiKey, baseCurrency, year, month, day);
		
		return webClient.get()
		        .uri(uri)
		        .retrieve()
		        .bodyToMono(String.class)
		        .map(json -> parseResponse(json, targetCurrencies))
		        .map(rates -> convertToEntity(key, rates))
		        .flatMap(rateService::saveMono);    // if you want save() async
	}
	
	 private CurrencyRate convertToEntity(String date, Map<String, Double> map) {
       return new CurrencyRate(
           date,
           map.get("USD") != null ? BigDecimal.ONE.divide(BigDecimal.valueOf(map.get("EUR")), MathContext.DECIMAL32) : null,
           map.get("USD") != null ? BigDecimal.ONE.divide(BigDecimal.valueOf(map.get("USD")), MathContext.DECIMAL32) : null,
           map.get("USD") != null ? BigDecimal.ONE.divide(BigDecimal.valueOf(map.get("CHF")), MathContext.DECIMAL32) : null,
           map.get("EUR") != null ? BigDecimal.ONE.divide(BigDecimal.valueOf(map.get("GBP")), MathContext.DECIMAL32) : null
       );
   }

	private Map<String, Double> parseResponse(String jsonResponse, String... targets) {
		try {
			JsonNode json      = objectMapper.readTree(jsonResponse);
			JsonNode ratesNode = json.path("conversion_rates");

			Set<String>         requested = new HashSet<>(Arrays.asList(targets));
			Map<String, Double> result    = new LinkedHashMap<>();

			ratesNode.fieldNames().forEachRemaining(currency -> {
				if (requested.isEmpty() || requested.contains(currency)) {
					result.put(currency, ratesNode.path(currency).asDouble());
				}
			});

			// Fill missing currencies with NaN
			for (String t : requested) {
				result.putIfAbsent(t, Double.NaN);
			}

			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyMap();
		}
	}
}
