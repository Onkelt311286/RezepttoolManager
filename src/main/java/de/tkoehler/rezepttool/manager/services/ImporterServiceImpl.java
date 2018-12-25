package de.tkoehler.rezepttool.manager.services;

import java.util.List;

import de.tkoehler.rezepttool.manager.application.mappers.ServiceRecipeToRepoRecipeMapper;
import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.Recipe;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParser;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParserException;

public class ImporterServiceImpl implements ImporterService {

	private final RecipeRepository recipeRepository;
	private final IngredientRepository ingredientRepository;
	private final RecipeParser recipeParser;
	private final ServiceRecipeToRepoRecipeMapper chefkochToRecipeMapper;

	public ImporterServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, RecipeParser recipeParser,
			ServiceRecipeToRepoRecipeMapper chefkochToRecipeMapper) {
		this.recipeRepository = recipeRepository;
		this.ingredientRepository = ingredientRepository;
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
		}
		catch (RecipeParserException e) {
			throw new ImporterServiceException("Failed to parse recipe!", e);
		}
	}

	public void updateRecipeWithKnownData(Recipe recipe) throws ImporterServiceException {
		checkNullParameter(recipe);
		List<Recipe> recipes = recipeRepository.findByUrlAndName(recipe.getUrl(), recipe.getName());
		if (recipes.size() > 0)
			throw new ImporterServiceException("Recipe already exists!");
		updateKnownIngredients(recipe.getIngredients());
	}

	public void updateKnownIngredients(List<RecipeIngredient> ingredients) throws ImporterServiceException {
		checkNullParameter(ingredients);
		for (RecipeIngredient recipeIngredient : ingredients) {
			for (String alternativeName : recipeIngredient.getIngredient().getAlternativeNames()) {
				List<Ingredient> ingredientNames = ingredientRepository.findByAlternativeName(alternativeName);
				if (ingredientNames.size() == 0) recipeIngredient.getIngredient().setName(alternativeName);
				else recipeIngredient.setIngredient(ingredientNames.get(0));
			}
		}
	}

	@Override
	public void saveRecipe(Recipe recipe) throws ImporterServiceException {
		checkNullParameter(recipe);
		recipeRepository.save(recipe);
	}

	private void checkNullParameter(Object parameter) throws ImporterServiceException {
		if (parameter == null)
			throw new ImporterServiceException("Parameter must not be empty!");
	}
}
