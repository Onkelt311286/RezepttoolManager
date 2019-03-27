package de.tkoehler.rezepttool.manager.restcontroller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

public interface RecipeOverviewController {
	ResponseEntity<List<TinyRecipe>> loadTinyRecipes();

	ResponseEntity<String> deleteRecipe(String id);

	ResponseEntity<RecipeWebInput> loadRecipe(String id);

	ResponseEntity<String> updateRecipe(RecipeWebInput updatedRecipe);
}
