package de.tkoehler.rezepttool.manager.services;

import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;
import de.tkoehler.rezepttool.manager.services.exceptions.ImporterServiceException;

public interface ImporterService {
	RecipeWebInput loadRecipeFromExternal(String urlString) throws ImporterServiceException;
}
