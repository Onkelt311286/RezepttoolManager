package de.tkoehler.rezepttool.manager.restcontroller;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.tkoehler.rezepttool.manager.repositories.model.TinyIngredient;
import de.tkoehler.rezepttool.manager.services.ImporterService;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@CrossOrigin
@RequestMapping("/recipe/create")
public class CreateRecipeControllerImpl implements CreateRecipeController {

	private ImporterService importerService;

	public CreateRecipeControllerImpl(ImporterService importerService) {
		this.importerService = importerService;
	}

	@Override
	@RequestMapping(path = "/ingredient/autocomplete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TinyIngredient>> autoCompleteIngredients(@RequestParam(required = false) String name, @RequestParam(required = false) String department) {
		List<TinyIngredient> result = new ArrayList<>();
		if (name != null && department != null)
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		else if (name != null)
			result = importerService.findIngredientNamesByName(name);
		else if (department != null)
			result = importerService.findDepartmentsByName(department);
		else return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@RequestMapping(path = "/ingredient/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TinyIngredient>> searchIngredients(@RequestParam(required = false) String name, @RequestParam(required = false) String department) {
		List<TinyIngredient> result = null;
		if (name != null && department != null)
			result = importerService.findAllTiniesByNameAndDepartment(name, department);
		else if (name != null)
			result = importerService.findAllTiniesByName(name);
		else if (department != null)
			result = importerService.findAllTiniesByDepartment(department);
		else result = importerService.findAllTinies();
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@RequestMapping(path = "/ingredient/loadTinies", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TinyIngredient>> loadTinyIngredients() {
		return new ResponseEntity<>(importerService.findAllTinies(), HttpStatus.OK);
	}

	@Override
	@RequestMapping(path = "/loadFromURL", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
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

	@Override
	@RequestMapping(path = "/save", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> saveRecipe(@Valid @RequestBody final RecipeWebInput newRecipe) {
		log.info("RequestBody: " + newRecipe);
		try {
			importerService.importRecipe(newRecipe);
		}
		catch (Exception e) {
			log.error("Fehler beim Erstellen!", e);
			return new ResponseEntity<>("\"" + e.getMessage() + "\"", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("\"success\"", HttpStatus.OK);
	}
}
