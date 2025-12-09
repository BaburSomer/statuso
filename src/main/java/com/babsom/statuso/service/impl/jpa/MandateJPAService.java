package com.babsom.statuso.service.impl.jpa;

import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.babsom.statuso.model.Mandate;
import com.babsom.statuso.repository.MandateRepository;
import com.babsom.statuso.service.MandateService;

@Service
@Profile({ "default", "springdatajpa" })
public class MandateJPAService implements MandateService {

	private final MandateRepository repository;

	public MandateJPAService(MandateRepository repository) {
		super();
		this.repository = repository;
	}

	@Override
	public Mandate findById(Long oid) {
		return repository.findById(oid).orElse(null);
	}

	@Override
	public void save(Mandate mandate) {
		repository.save(mandate);
	}

	@Override
	public void deleteByObject(Mandate mandate) {
		repository.delete(mandate);
	}

	@Override
	public void deleteById(Long oid) {
		repository.deleteById(oid);
	}

	@Override
	public Mandate findByName(String name) {
		return repository.findByName(name);
	}

	@Override
	public Set<Mandate> findAll() {
		Set<Mandate> mandates = new HashSet<>();
		repository.findAll().forEach(mandates::add);
		return mandates;
	}
}