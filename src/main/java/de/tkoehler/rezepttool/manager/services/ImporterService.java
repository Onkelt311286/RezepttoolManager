package de.tkoehler.rezepttool.manager.services;

import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

public interface ImporterService {
	RecipeWebInput importRecipe(String urlString) throws ImporterServiceException;
}
