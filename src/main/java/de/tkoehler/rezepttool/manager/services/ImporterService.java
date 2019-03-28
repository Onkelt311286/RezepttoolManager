package de.tkoehler.rezepttool.manager.services;

import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;

public interface ImporterService {
	RecipeWebInput loadRecipeFromExternal(String urlString) throws ImporterServiceException;
}
