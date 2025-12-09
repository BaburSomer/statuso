package com.babsom.statuso.service;

import com.babsom.statuso.model.Mandate;

public interface MandateService extends CRUDService<Mandate, Long> {

	Mandate findByName(String name);
}