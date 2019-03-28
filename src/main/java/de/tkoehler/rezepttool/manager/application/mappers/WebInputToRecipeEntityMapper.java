package de.tkoehler.rezepttool.manager.application.mappers;

import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;

public interface WebInputToRecipeEntityMapper {

	RecipeEntity process(RecipeWebInput recipe);

}