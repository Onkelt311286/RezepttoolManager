package de.tkoehler.rezepttool.manager.restcontroller;

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

import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.services.ManagerService;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@CrossOrigin
@RequestMapping("/recipe/overview")
public class RecipeOverviewControllerImpl implements RecipeOverviewController {
	
	private ManagerService managerService;
	
	public RecipeOverviewControllerImpl(ManagerService managerService) {
		this.managerService = managerService;
	}

	@Override
	@RequestMapping(path = "/loadTinies", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TinyRecipe>> loadTinyRecipes() {
		return new ResponseEntity<>(managerService.findAllTinies(), HttpStatus.OK);
	}

	@Override
	@RequestMapping(path = "/delete", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteRecipe(@RequestParam String id) {
		try {
			managerService.deleteRecipe(id);
		}
		catch (Exception e) {
			log.info("Error");
			return new ResponseEntity<>("\"Error\"", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Success");
		return new ResponseEntity<>("\"Success\"", HttpStatus.OK);
	}
	
	@Override
	@RequestMapping(path = "/load", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RecipeWebInput> loadRecipe(@RequestParam String id) {
		RecipeWebInput result = null;
		try {
			result = managerService.loadRecipe(id);
		}
		catch (Exception e) {
			log.info("Error");
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@Override
	@RequestMapping(path = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> updateRecipe(@Valid @RequestBody final RecipeWebInput updatedRecipe) {
		log.info("RequestBody: " + updatedRecipe);
		try {
			managerService.updateRecipe(updatedRecipe);
		}
		catch (Exception e) {
			log.error("Fehler beim Erstellen!", e);
			return new ResponseEntity<>("\"" + e.getMessage() + "\"", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("\"success\"", HttpStatus.OK);
	}
}
