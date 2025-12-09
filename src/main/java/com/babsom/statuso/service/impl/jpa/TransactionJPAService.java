package com.babsom.statuso.service.impl.jpa;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.babsom.statuso.model.Transaction;
import com.babsom.statuso.repository.TransactionRepository;
import com.babsom.statuso.service.ExchangeRatesFetchService;
import com.babsom.statuso.service.TransactionService;

@Service
@Profile({ "default", "springdatajpa" })
public class TransactionJPAService extends AbstractJPAService<Transaction, Long, TransactionRepository> implements TransactionService {

	private final ExchangeRatesFetchService fxRatesService;
	private final String[]                  targetCurrencies = { "EUR", "USD", "CHF", "GBP" };
	private final String                    baseCurrency;

	public TransactionJPAService(CrudRepository<Transaction, Long> repository, ExchangeRatesFetchService fxRatesService) {
		super(repository);
		this.fxRatesService = fxRatesService;
		this.baseCurrency   = "TRY";
	}

	@Override
	public Transaction findById(Long oid) {
		return findById(oid);
	}

	@Override
	public void deleteById(Long oid) {
		deleteById(oid);
	}

	@Override
	public List<Transaction> obtainTransactions() {
		return new ArrayList<>();
	}

	@Override
	public Transaction obtainTransaction(Transaction transaction) {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(Transaction object) {
		if (Objects.isNull(object)) {
			throw new RuntimeException("Object cannot be null!");
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(object.getTimestamp().getTime());

		fxRatesService.getOrFetchExchangeRate(baseCurrency, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
				calendar.get(Calendar.DAY_OF_MONTH), targetCurrencies).block();

		Transaction transaction = obtainTransaction(object);
		if (Objects.isNull(transaction)) {
			super.save(object);
		}
	}
}