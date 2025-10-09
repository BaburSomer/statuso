package com.babsom.statuso.bootstrap;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StatusoBootstrap implements ApplicationListener<ContextRefreshedEvent>{
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		log.debug(">>> - Bootstrap");
		log.debug("<<< - Bootstrap");
	}
}