package de.tkoehler.rezepttool.manager.services;

import de.tkoehler.rezepttool.manager.repositories.RezepttoolRepository;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParser;

public class ImporterServiceImpl implements ImporterService {

	private final RezepttoolRepository rezepttoolRepository;
	private final RecipeParser recipeParser;

	public ImporterServiceImpl(RezepttoolRepository rezepttoolRepository, RecipeParser recipeParser) {
		super();
		this.rezepttoolRepository = rezepttoolRepository;
		this.recipeParser = recipeParser;
	}

	@Override
	public void importRecipe(String urlString) throws ImporterServiceException {

	}
}
