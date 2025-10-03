package com.babsom.statuso.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.babsom.statuso.model.Transaction;
import com.babsom.statuso.repository.AccountMovementRepository;
import com.babsom.statuso.service.AccountMovementService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountMovementServiceImpl implements AccountMovementService {

	private final AccountMovementRepository repository;

	public AccountMovementServiceImpl(AccountMovementRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Set<Transaction> getAccountMovements() {
		log.debug(">>> getAccountMovements");
		Set<Transaction> transactions = new HashSet<>();
		log.debug("obtaining existing account novements");
		getRepository().findAll().iterator().forEachRemaining(transactions::add);
		log.debug("<<< getAccountMovements");
		return transactions;
	}

	public AccountMovementRepository getRepository() {
		return this.repository;
	}
}
