package de.tkoehler.rezepttool.manager.restcontroller;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/rezept/create")
public class CreateRecipeController {
	@CrossOrigin
	@RequestMapping(path = "/init", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<URL> initializeCreateRecipePage() throws MalformedURLException {
		log.info("init createRecipe.html");
		URL url = new URL("https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html");
		return new ResponseEntity<>(url, HttpStatus.OK);
	}
}
