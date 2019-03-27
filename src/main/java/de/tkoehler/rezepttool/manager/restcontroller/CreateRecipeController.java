package de.tkoehler.rezepttool.manager.restcontroller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import de.tkoehler.rezepttool.manager.repositories.model.TinyIngredient;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

public interface CreateRecipeController {

	ResponseEntity<List<TinyIngredient>> autoCompleteIngredients(String name, String department);

	ResponseEntity<List<TinyIngredient>> searchIngredients(String name, String department);

	ResponseEntity<List<TinyIngredient>> loadTinyIngredients();

	ResponseEntity<RecipeWebInput> loadRecipeFromExternalURL(String json);

	ResponseEntity<String> saveRecipe(RecipeWebInput newRecipe);

}