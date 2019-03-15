package de.tkoehler.rezepttool.manager.restcontroller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.tkoehler.rezepttool.manager.services.ImporterService;
import de.tkoehler.rezepttool.manager.services.ImporterServiceException;
import de.tkoehler.rezepttool.manager.services.ManagerService;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/recipe/")
public class CreateRecipeControllerImpl implements CreateRecipeController{
	
	private ImporterService importerService;
	private ManagerService managerService;
	
	public CreateRecipeControllerImpl(ImporterService importerService, ManagerService managerService) {
		this.importerService = importerService;
		this.managerService = managerService;
	}
	
	@CrossOrigin
	@RequestMapping(path = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RecipeWebInput> loadRecipe(@RequestBody final String json) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		log.info(json);
		
		String recipeUrl = mapper.readValue(json, String.class);
		log.info("Loading : " + recipeUrl);
		RecipeWebInput result = null;
		try {
			result = importerService.importRecipe(recipeUrl);
		}
		catch (ImporterServiceException e) {
			log.error("Fehler beim Erstellen!", e);
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Loaded");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@CrossOrigin
	@RequestMapping(path = "/init", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<URL> initializeCreateRecipePage() throws MalformedURLException {
		log.info("init createRecipe.html");
		URL url = new URL("https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html");
		return new ResponseEntity<>(url, HttpStatus.OK);
	}
}
