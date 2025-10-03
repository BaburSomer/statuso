package com.babsom.statuso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.babsom.statuso.service.AccountMovementService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class IndexController {

	private final AccountMovementService service;

	public IndexController(AccountMovementService service) {
		super();
		this.service = service;
	}

	@GetMapping({ "", "/", "index" })
	public String getIndexPage(Model model) {
		log.debug("creating index page");
		model.addAttribute("movements", service.getAccountMovements());
		return "index";
	}
}
