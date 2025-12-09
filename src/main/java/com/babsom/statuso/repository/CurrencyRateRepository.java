package com.babsom.statuso.repository;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.babsom.statuso.model.CurrencyRate;

@Service
public interface CurrencyRateRepository extends CrudRepository<CurrencyRate, String> {

	@Cacheable(cacheNames = "currencyRateCache", key = "#date")
	Optional<CurrencyRate> findByDate(String date);
}
