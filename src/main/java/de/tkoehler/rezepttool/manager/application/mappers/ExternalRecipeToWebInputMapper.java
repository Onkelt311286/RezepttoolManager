package de.tkoehler.rezepttool.manager.application.mappers;

import de.tkoehler.rezepttool.manager.services.model.Recipe;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInputCreate;

public interface ExternalRecipeToWebInputMapper {
	RecipeWebInputCreate process(Recipe recipe);
}
