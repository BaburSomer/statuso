package com.babsom.statuso.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExchangeRatesFetchServiceOLD {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;
	 private int idx;
    
    public ExchangeRatesFetchServiceOLD(WebClient.Builder webClientBuilder) {
   	 this.webClient = WebClient.create("https://v6.exchangerate-api.com");
   	 this.objectMapper = new ObjectMapper();
    }

    /**
     * Gets the USD→TRY rate for the given date (format YYYY-MM-DD).
     */
		public Map<String, Double> getUsdTryRateForDate(String baseCurrency, int year, int month, int day, String... targetCurrencies) {
			Map<String, Double> returnMap = new HashMap<>();
			idx = 0;
			webClient.get().uri("/v6/8c4135691a4c0dce473ca37b/history/{baseCurrency}/{year}/{month}/{day}", baseCurrency, year, month, day).retrieve().bodyToMono(String.class)
					.map(response -> {
						try {
							JsonNode json = objectMapper.readTree(response);
							JsonNode rates = json.path("conversion_rates");
							for (String targetCurrency : targetCurrencies) {
								returnMap.put(targetCurrency, rates.path(targetCurrencies[idx++]).asDouble());
							}
							return 0.0;
						} catch (Exception e) {
							e.printStackTrace();
							returnMap.put(targetCurrencies[idx], 0.0);
							return 0.0;
						}
					});
			return returnMap;
		}
}
