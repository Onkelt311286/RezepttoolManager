package de.tkoehler.rezepttool.manager.application.mappers;

import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInputCreate;

public interface WebInputToRecipeEntityMapper {

	RecipeEntity process(RecipeWebInputCreate recipe);

}