package de.tkoehler.rezepttool.manager.services;

import de.tkoehler.rezepttool.manager.web.model.RecipeWebInputCreate;

public interface ImporterService {

	RecipeWebInputCreate loadRecipe(String urlString) throws ImporterServiceException;

	void saveRecipe(RecipeWebInputCreate recipe) throws ImporterServiceException;

}