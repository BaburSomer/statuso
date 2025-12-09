package com.babsom.statuso.service;

import java.util.List;

import com.babsom.statuso.model.Transaction;

public interface TransactionService extends CRUDService<Transaction, Long> {
	List<Transaction> obtainTransactions();
	Transaction obtainTransaction(Transaction transaction);
}