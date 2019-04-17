package de.tkoehler.rezepttool.manager.restcontroller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;

import de.tkoehler.rezepttool.manager.repositories.model.FilterableRecipe;
import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.restcontroller.model.DailyPlanWebInput;
import de.tkoehler.rezepttool.manager.restcontroller.model.GroceryPlan;
import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;

public interface RecipePlannerController {
	ResponseEntity<List<FilterableRecipe>> loadFilterableRecipes();

	ResponseEntity<List<RecipeWebInput>> loadRecipes(TinyRecipe[] recipes);

	ResponseEntity<String> checkIngredient(String checkIngredientJson);

	ResponseEntity<String> planRecipe(DailyPlanWebInput plan);

	ResponseEntity<List<DailyPlanWebInput>> loadPlans(List<DailyPlanWebInput> plans);

	ResponseEntity<GroceryPlan> loadIngredients(@Valid DailyPlanWebInput[] plans);
}
