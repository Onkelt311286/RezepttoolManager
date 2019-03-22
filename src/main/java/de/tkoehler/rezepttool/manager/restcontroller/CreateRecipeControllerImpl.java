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

import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.model.TinyIngredient;
import de.tkoehler.rezepttool.manager.services.ImporterService;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@CrossOrigin
@RequestMapping("/recipe")
public class CreateRecipeControllerImpl implements CreateRecipeController {

	private ImporterService importerService;
	private IngredientRepository ingredientRepository;

	public CreateRecipeControllerImpl(ImporterService importerService, IngredientRepository ingredientRepository) {
		this.importerService = importerService;
		this.ingredientRepository = ingredientRepository;
	}

	@RequestMapping(path = "/ingredient/autocomplete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TinyIngredient>> autoCompleteIngredients(@RequestParam(required = false) String name, @RequestParam(required = false) String department) {
		List<TinyIngredient> result = new ArrayList<>();
		if (name != null && department != null)
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		else if (name != null)
			ingredientRepository.findIngredientNamesByName(name).stream().forEach(n -> result.add(TinyIngredient.builder().name(n).department(n).build()));
		else if (department != null)
			ingredientRepository.findDepartmentsByName(department).stream().forEach(n -> result.add(TinyIngredient.builder().name(n).department(n).build()));
		else return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(path = "/ingredient/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TinyIngredient>> searchIngredients(@RequestParam(required = false) String name, @RequestParam(required = false) String department) {
		List<TinyIngredient> result = null;
		if (name != null && department != null)
			result = ingredientRepository.findAllTiniesByNameAndDepartment(name, department);
		else if (name != null)
			result = ingredientRepository.findAllTiniesByName(name);
		else if (department != null)
			result = ingredientRepository.findAllTiniesByDepartment(department);
		else result = ingredientRepository.findAllTinies();
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@RequestMapping(path = "/loadTinyIngredients", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TinyIngredient>> loadTinyIngredients() {
		return new ResponseEntity<>(ingredientRepository.findAllTinies(), HttpStatus.OK);
	}

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
