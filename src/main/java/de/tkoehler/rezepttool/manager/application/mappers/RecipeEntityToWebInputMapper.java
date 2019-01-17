package de.tkoehler.rezepttool.manager.application.mappers;

import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInputEdit;

public interface RecipeEntityToWebInputMapper {

	RecipeWebInputEdit process(RecipeEntity recipe);

}
