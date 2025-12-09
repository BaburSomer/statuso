package com.babsom.statuso.service;

import com.babsom.statuso.model.FactoryCreationRule;

public interface FactoryCreationRuleService extends CRUDService<FactoryCreationRule, Long> {

	FactoryCreationRule findByRule(String rule);
}