package com.babsom.statuso.service.impl.jpa;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.babsom.statuso.model.FactoryCreationRule;
import com.babsom.statuso.repository.FactoryCreationRuleRepository;
import com.babsom.statuso.service.FactoryCreationRuleService;

@Service
@Profile({ "default", "springdatajpa" })
public class FactoryCreationRuleJPAService extends AbstractJPAService<FactoryCreationRule, Long, FactoryCreationRuleRepository> implements FactoryCreationRuleService {

	public FactoryCreationRuleJPAService(CrudRepository<FactoryCreationRule, Long> repository) {
		super(repository);
	}

	@Override
	public FactoryCreationRule findByRule(String rule) {
		return ((FactoryCreationRuleRepository)getRepository()).findByRule(rule);
	}

	@Override
	public FactoryCreationRule findById(Long oid) {
		return findById(oid);
	}

	@Override
	public void deleteById(Long oid) {
		deleteById(oid);
	}
}