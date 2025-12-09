package com.babsom.statuso.service.impl.jpa;

import java.util.HashSet;
import java.util.Set;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.babsom.statuso.model.CurrencyRate;
import com.babsom.statuso.repository.CurrencyRateRepository;
import com.babsom.statuso.service.CurrencyRateService;

import reactor.core.publisher.Mono;

@Service
@Profile({ "default", "springdatajpa" })
public class CurrencyRateJPAService implements CurrencyRateService {

	private final CurrencyRateRepository repository;

	public CurrencyRateJPAService(CurrencyRateRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Set<CurrencyRate> findAll() {
		Set<CurrencyRate> rates = new HashSet<>();
		repository.findAll().forEach(r -> rates.add(r));
//		repository.findAll().forEach(rates::add);
		return  rates;
	}

	@Override
	@CachePut(cacheNames = "currencyRateCache", key = "#rate.date")
	public void save(CurrencyRate rate) {
		repository.save(rate);
	}

	@Override
	public void deleteByObject(CurrencyRate currencyRate) {
		repository.delete(currencyRate);
	}

	@Override
	@Cacheable(cacheNames = "currencyRateCache", key = "#date")
	public CurrencyRate getRate(String date) {
		return repository.findByDate(date).orElse(null);
	}

	@Override
	public CurrencyRate findById(String date) {
		return this.getRate(date);
	}

	@Override
	public void deleteById(String date) {
		repository.deleteById(date);
	}
	
	@CachePut(cacheNames = "currencyRates", key = "#rate.date")
	public CurrencyRate saveBlocking(CurrencyRate rate) {
	    return repository.save(rate);
	}
	
	@Override
   public Mono<CurrencyRate> saveMono(CurrencyRate rate) {
       return Mono.fromCallable(() -> repository.save(rate));
   }
}