package com.babsom.statuso.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.babsom.statuso.model.Mandate;

@Service
public interface MandateRepository extends CrudRepository<Mandate, Long> {

	Mandate findByName(String name);
}
