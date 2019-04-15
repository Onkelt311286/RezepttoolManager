package de.tkoehler.rezepttool.manager.restcontroller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.tkoehler.rezepttool.manager.repositories.model.FilterableRecipe;
import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.restcontroller.model.DailyPlanWebInput;
import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;
import de.tkoehler.rezepttool.manager.services.EditorService;
import de.tkoehler.rezepttool.manager.services.ManagerService;
import de.tkoehler.rezepttool.manager.services.PlannerService;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@CrossOrigin
@RequestMapping("/recipe/planner")
public class RecipePlannerControllerImpl implements RecipePlannerController {

	private ManagerService managerService;
	private EditorService editorService;
	private PlannerService plannerService;

	public RecipePlannerControllerImpl(ManagerService managerService, EditorService editorService, PlannerService plannerService) {
		this.managerService = managerService;
		this.editorService = editorService;
		this.plannerService = plannerService;
	}

	@Override
	@RequestMapping(path = "/loadFilterables", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<FilterableRecipe>> loadFilterableRecipes() {
		return new ResponseEntity<>(managerService.findAllFilterableRecipes(), HttpStatus.OK);
	}

	@Override
	@RequestMapping(path = "/loadRecipes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<RecipeWebInput>> loadRecipes(@Valid @RequestBody final TinyRecipe[] recipes) {
		log.info("RequestBody: " + recipes);
		List<RecipeWebInput> result = new ArrayList<>();
		try {
			for (TinyRecipe tinyRecipe : recipes) {
				result.add(editorService.loadRecipe(tinyRecipe.getId()));
			}
		}
		catch (Exception e) {
			log.info("Error");
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@RequestMapping(path = "/loadPlans", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<DailyPlanWebInput>> loadPlans(@Valid @RequestBody final List<DailyPlanWebInput> plans) {
		List<DailyPlanWebInput> result = new ArrayList<>();
		log.info("Plans: " + plans.toString());
		for (DailyPlanWebInput plan : plans) {
			log.info("Plan: " + plan.toString());
		}
		try {
			for (DailyPlanWebInput plan : plans) {
				result.add(plannerService.loadPlan(plan));
			}
		}
		catch (Exception e) {
			log.info("Error", e);
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		log.info("Success");
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@RequestMapping(path = "/check", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> checkIngredient(@RequestBody final String checkIngredientJson) {
		log.info("RequestBody: " + checkIngredientJson);
		JSONParser parser = new JSONParser();
		JSONObject json = null;
		try {
			json = (JSONObject) parser.parse(checkIngredientJson);
			String id = json.get("id").toString();
			Boolean present = (Boolean) json.get("present");
			plannerService.checkIngredient(id, present);
		}
		catch (Exception e) {
			return new ResponseEntity<>("\"" + e.getMessage() + "\"", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("\"success\"", HttpStatus.OK);
	}

	@Override
	@RequestMapping(path = "/plan", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> planRecipe(@RequestBody final DailyPlanWebInput plan) {
		try {
			if (plan.getRecipes().size() == 0)
				plannerService.deletePlan(plan);
			else plannerService.updatePlan(plan);
		}
		catch (Exception e) {
			return new ResponseEntity<>("\"" + e.getMessage() + "\"", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>("\"success\"", HttpStatus.OK);
	}
}
