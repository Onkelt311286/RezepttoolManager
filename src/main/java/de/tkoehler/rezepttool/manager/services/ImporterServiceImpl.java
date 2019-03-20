package de.tkoehler.rezepttool.manager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.tkoehler.rezepttool.manager.application.mappers.ExternalRecipeToWebInputMapper;
import de.tkoehler.rezepttool.manager.application.mappers.WebInputToRecipeEntityMapper;
import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParser;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParserException;
import de.tkoehler.rezepttool.manager.web.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@Component
@Transactional
public class ImporterServiceImpl implements ImporterService {

	private final RecipeParser recipeParser;
	private final RecipeRepository recipeRepository;
	private final IngredientRepository ingredientRepository;
	private final ExternalRecipeToWebInputMapper externalRecipeToWebInputMapper;
	private final WebInputToRecipeEntityMapper webInputToRecipeEntityMapper;

	public ImporterServiceImpl(RecipeParser recipeParser, RecipeRepository recipeRepository, IngredientRepository ingredientRepository, ExternalRecipeToWebInputMapper externalRecipeToWebInputMapper,
			WebInputToRecipeEntityMapper webInputToRecipeEntityMapper) {
		super();
		this.recipeParser = recipeParser;
		this.recipeRepository = recipeRepository;
		this.ingredientRepository = ingredientRepository;
		this.externalRecipeToWebInputMapper = externalRecipeToWebInputMapper;
		this.webInputToRecipeEntityMapper = webInputToRecipeEntityMapper;
	}

	@Override
	public RecipeWebInput loadRecipe(String urlString) throws ImporterServiceException {
		checkNullParameter(urlString);
		try {
			RecipeWebInput recipe = externalRecipeToWebInputMapper.process(recipeParser.parseRecipe(urlString));
			updateWebRecipeWithKnownData(recipe);
			return recipe;
		}
		catch (RecipeParserException e) {
			throw new ImporterServiceException("Failed to parse recipe!", e);
		}
	}

	public void updateWebRecipeWithKnownData(RecipeWebInput recipe) throws ImporterServiceException {
		checkNullParameter(recipe);
		for (IngredientWebInput ingredient : recipe.getIngredients()) {
			updateWebIngredientWithKnownData(ingredient);
		}
	}

	public void updateWebIngredientWithKnownData(IngredientWebInput ingredient) throws ImporterServiceException {
		checkNullParameter(ingredient);
		List<Ingredient> ingredients = ingredientRepository.findByAlternativeName(ingredient.getOriginalName());
		if (ingredients.size() > 0) {
			StringBuilder names = new StringBuilder();
			StringBuilder departments = new StringBuilder();
			for (int i = 0; i < ingredients.size(); i++) {
				Ingredient ingredientEntity = ingredients.get(i);
				names.append(ingredientEntity.getName());
				departments.append(ingredientEntity.getDepartment());
				if (i + 1 < ingredients.size()) {
					names.append(" | ");
					departments.append(" | ");
				}
			}
			ingredient.setDepartment(departments.toString());
			ingredient.setName(names.toString());
		}
	}

	@Override
	public void importRecipe(RecipeWebInput newRecipe) throws ImporterServiceException {
		checkNullParameter(newRecipe);
		checkForExistingRecipe(newRecipe);
		RecipeEntity recipe = webInputToRecipeEntityMapper.process(newRecipe);
		for (RecipeIngredient ingredient : recipe.getIngredients()) {
			updateKnownIngredient(ingredient.getIngredient());
		}
		recipeRepository.save(recipe);
	}

	private void checkForExistingRecipe(RecipeWebInput recipe) throws ImporterServiceRecipeExistsException {
		List<RecipeEntity> recipes = recipeRepository.findByUrlAndName(recipe.getUrl(), recipe.getName());
		if (recipes.size() > 0)
			throw new ImporterServiceRecipeExistsException("Recipe already exists!");
	}

	public void updateKnownIngredient(Ingredient ingredient) throws ImporterServiceException {
		checkNullParameter(ingredient);
		Optional<Ingredient> ingredientEntity = ingredientRepository.findByNameAndDepartment(ingredient.getName(), ingredient.getDepartment());
		if (ingredientEntity.isPresent()) {
			ingredient.setId(ingredientEntity.get().getId());
			ingredient.getAlternativeNames().addAll(ingredientEntity.get().getAlternativeNames());
		}
	}

	private void checkNullParameter(Object parameter) throws ImporterServiceException {
		if (parameter == null) throw new ImporterServiceException("Parameter must not be empty!");
	}
}
