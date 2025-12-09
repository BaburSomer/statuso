package com.babsom.statuso.cache;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.babsom.statuso.repository.CurrencyRateRepository;

@Component
public class CurrencyRateCacheLoader {

    private final CurrencyRateRepository repo;
    private final CacheManager cacheManager;

    public CurrencyRateCacheLoader(CurrencyRateRepository repo, CacheManager cacheManager) {
        this.repo = repo;
        this.cacheManager = cacheManager;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void preload() {
        Cache cache = cacheManager.getCache("currencyRateCache");
        if (cache == null) return;

        repo.findAll().forEach(rate ->
            cache.put(rate.getDate(), rate)
        );

        System.out.println("Loaded " + repo.count() + " currency rates into cache.");
    }
}
