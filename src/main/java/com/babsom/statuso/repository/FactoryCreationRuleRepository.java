package com.babsom.statuso.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.babsom.statuso.model.FactoryCreationRule;

@Service
public interface FactoryCreationRuleRepository extends CrudRepository<FactoryCreationRule, Long> {

	FactoryCreationRule findByRule(String rule);
}
