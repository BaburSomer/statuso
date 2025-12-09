package com.babsom.statuso.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.babsom.statuso.model.Mandate;
import com.babsom.statuso.service.MandateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StatusoBootstrap implements ApplicationListener<ContextRefreshedEvent>{
	private final MandateService mandateService;

	public StatusoBootstrap(MandateService mandateService) {
		super();
		this.mandateService = mandateService;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.debug(">>> - Bootstrap");
		this.loadData();
		log.debug("<<< - Bootstrap");
	}

	private void loadData() {
		log.debug(">>> - loadData");
		this.loadMandates();
		log.debug("<<< - loadData");
	}

	private void loadMandates() {
		log.debug(">>> - loadMandates");

		Mandate mandate = new Mandate();
		mandate.setName("ElifBab");
		mandate.setDescr("ElifBab's Data");
		this.mandateService.save(mandate);

		Mandate testMandate = new Mandate();
		testMandate.setName("Test");
		testMandate.setDescr("Mandate for Testing Purposes");
		this.mandateService.save(testMandate);
		
		log.debug("<<< - loadMandates");
	}
}