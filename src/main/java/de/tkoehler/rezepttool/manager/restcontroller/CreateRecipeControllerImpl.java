package de.tkoehler.rezepttool.manager.restcontroller;

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
@RequestMapping("/recipe/")
public class CreateRecipeControllerImpl implements CreateRecipeController {

	private ImporterService importerService;

	public CreateRecipeControllerImpl(ImporterService importerService) {
		this.importerService = importerService;
	}

	@CrossOrigin
	@RequestMapping(path = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RecipeWebInput> loadRecipe(@RequestBody final String json) {
		log.info(json);
		RecipeWebInput result = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			result = importerService.importRecipe(mapper.readValue(json, String.class));
		}
		catch (Exception e) {
			log.error("Fehler beim Erstellen!", e);
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
