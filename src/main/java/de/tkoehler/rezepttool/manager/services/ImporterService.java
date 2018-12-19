package de.tkoehler.rezepttool.manager.services;

import de.tkoehler.rezepttool.manager.repositories.model.Recipe;

public interface ImporterService {

	Recipe loadRecipe(String urlString) throws ImporterServiceException;
	void saveRecipe(Recipe recipe) throws ImporterServiceException;

}