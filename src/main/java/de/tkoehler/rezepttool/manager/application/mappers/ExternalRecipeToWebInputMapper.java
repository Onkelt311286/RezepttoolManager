package de.tkoehler.rezepttool.manager.application.mappers;

import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;
import de.tkoehler.rezepttool.manager.services.model.Recipe;

public interface ExternalRecipeToWebInputMapper {
	RecipeWebInput process(Recipe recipe);
}
