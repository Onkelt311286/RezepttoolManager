package de.tkoehler.rezepttool.manager.application.mappers;

import de.tkoehler.rezepttool.manager.services.model.Recipe;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

public interface ExternalRecipeToWebInputMapper {
	RecipeWebInput process(Recipe recipe);
}
