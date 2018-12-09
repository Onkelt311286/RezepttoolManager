package de.tkoehler.rezepttool.manager.services;

public interface ImporterService {

	void importRecipe(String urlString) throws ImporterServiceException;

}