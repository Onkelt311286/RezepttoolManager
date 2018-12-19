package de.tkoehler.rezepttool.manager.services;

import de.tkoehler.rezepttool.manager.application.mappers.ServiceRecipeToRepoRecipeMapper;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Recipe;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParser;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParserException;

public class ImporterServiceImpl implements ImporterService {

	private final RecipeRepository recipeRepository;
	private final RecipeParser recipeParser;
	private final ServiceRecipeToRepoRecipeMapper chefkochToRecipeMapper;

	public ImporterServiceImpl(RecipeRepository recipeRepository, RecipeParser recipeParser,
			ServiceRecipeToRepoRecipeMapper chefkochToRecipeMapper) {
		super();
		this.recipeRepository = recipeRepository;
		this.recipeParser = recipeParser;
		this.chefkochToRecipeMapper = chefkochToRecipeMapper;
	}

	@Override
	public Recipe loadRecipe(String urlString) throws ImporterServiceException {
		checkNullParameter(urlString);
		try {
			Recipe recipe = chefkochToRecipeMapper.process(recipeParser.parseRecipe(urlString));
			updateRecipeWithKnownData(recipe);
			return recipe;
		} catch (RecipeParserException e) {
			throw new ImporterServiceException("Failed to parse recipe!", e);
		}
	}

	private void updateRecipeWithKnownData(Recipe recipe) throws ImporterServiceException {
		checkNullParameter(recipe);

	}
	
	@Override
	public void saveRecipe(Recipe recipe) throws ImporterServiceException {
		// TODO Auto-generated method stub
		
	}

	private void checkNullParameter(Object parameter) throws ImporterServiceException {
		if (parameter == null)
			throw new ImporterServiceException("Parameter must not be empty!");
	}
}
