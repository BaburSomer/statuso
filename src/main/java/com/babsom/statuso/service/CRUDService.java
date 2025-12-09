package com.babsom.statuso.service;

import java.util.Set;

public interface CRUDService<T, ID> {

	T findById(ID oid);

	Set<T> findAll();

	void save(T object);

	void deleteById(ID oid);

	void deleteByObject(T object);
	
}