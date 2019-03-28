package de.tkoehler.rezepttool.manager.restcontroller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.tkoehler.rezepttool.manager.repositories.model.FilterableRecipe;
import de.tkoehler.rezepttool.manager.services.ManagerService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@CrossOrigin
@RequestMapping("/recipe/planner")
public class RecipePlannerControllerImpl implements RecipePlannerController {

	private ManagerService managerService;

	public RecipePlannerControllerImpl(ManagerService managerService) {
		this.managerService = managerService;
	}

	@Override
	@RequestMapping(path = "/loadFilterables", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FilterableRecipe>> loadFilterableRecipes() {
		return new ResponseEntity<>(managerService.findAllFilterableRecipes(), HttpStatus.OK);
	}
}
