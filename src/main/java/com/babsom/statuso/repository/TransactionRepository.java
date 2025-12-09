package com.babsom.statuso.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.babsom.statuso.model.Transaction;

@Service
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
	
}
