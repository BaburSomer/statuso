package com.babsom.statuso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StatusoApplication {

	public static void main(String[] args) {
		SpringApplication.run(StatusoApplication.class, args);
	}

}
