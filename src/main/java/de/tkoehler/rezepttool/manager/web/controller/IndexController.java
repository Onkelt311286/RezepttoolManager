package de.tkoehler.rezepttool.manager.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class IndexController {
	@GetMapping("/")
	public String index() {
		log.info("Main Page");
		return "index";
	}
}
