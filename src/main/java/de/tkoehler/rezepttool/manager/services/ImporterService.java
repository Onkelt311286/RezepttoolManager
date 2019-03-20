package de.tkoehler.rezepttool.manager.services;

import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

public interface ImporterService {
	RecipeWebInput loadRecipe(String urlString) throws ImporterServiceException;
	void importRecipe(RecipeWebInput newRecipe) throws ImporterServiceException;
}
