package de.tkoehler.rezepttool.manager.restcontroller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.tkoehler.rezepttool.manager.services.ImporterService;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@CrossOrigin
@RequestMapping("/recipe")
public class CreateRecipeControllerImpl implements CreateRecipeController {

	private ImporterService importerService;

	public CreateRecipeControllerImpl(ImporterService importerService) {
		this.importerService = importerService;
	}

	@RequestMapping(path = "/load", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RecipeWebInput> loadRecipeFromExternalURL(@RequestBody final String json) {
		log.info("RequestBody: " + json);
		RecipeWebInput result = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			result = importerService.loadRecipe(mapper.readValue(json, String.class));
		}
		catch (Exception e) {
			log.error("Fehler beim Erstellen!", e);
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(path = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveRecipe(@Valid @RequestBody final RecipeWebInput newRecipe) {
		log.info("RequestBody: " + newRecipe);
		try {
			importerService.importRecipe(newRecipe);
		}
		catch (Exception e) {
			log.error("Fehler beim Erstellen!", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("\"" + UUID.randomUUID().toString() + "\"", HttpStatus.OK);
	}
}
