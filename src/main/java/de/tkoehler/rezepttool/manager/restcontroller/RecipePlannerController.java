package de.tkoehler.rezepttool.manager.restcontroller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import de.tkoehler.rezepttool.manager.repositories.model.FilterableRecipe;

public interface RecipePlannerController {
	ResponseEntity<List<FilterableRecipe>> loadFilterableRecipes();
}
