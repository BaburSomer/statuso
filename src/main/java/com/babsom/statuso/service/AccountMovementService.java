package com.babsom.statuso.service;

import java.util.Set;

import com.babsom.statuso.model.Transaction;

public interface AccountMovementService {

	Set<Transaction> getAccountMovements();

}
