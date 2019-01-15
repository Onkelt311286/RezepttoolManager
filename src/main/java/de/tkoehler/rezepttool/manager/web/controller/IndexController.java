package de.tkoehler.rezepttool.manager.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import de.tkoehler.rezepttool.manager.web.model.UrlWrapper;
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
