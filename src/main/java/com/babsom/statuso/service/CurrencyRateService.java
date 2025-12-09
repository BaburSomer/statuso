package com.babsom.statuso.service;

import com.babsom.statuso.model.CurrencyRate;

import reactor.core.publisher.Mono;

public interface CurrencyRateService extends CRUDService<CurrencyRate, String> {
	
	CurrencyRate getRate(String key);

	// Reactive saving
   public Mono<CurrencyRate> saveMono(CurrencyRate rate);
}