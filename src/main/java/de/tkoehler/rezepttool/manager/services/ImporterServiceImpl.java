package de.tkoehler.rezepttool.manager.services;

import java.util.List;

import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.application.mappers.ExternalRecipeToWebInputMapper;
import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParser;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParserException;
import de.tkoehler.rezepttool.manager.web.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ImporterServiceImpl implements ImporterService {

	private final RecipeParser recipeParser;
	private final IngredientRepository ingredientRepository;
	private final ExternalRecipeToWebInputMapper externalRecipeToWebInputMapper;

	public ImporterServiceImpl(RecipeParser recipeParser, IngredientRepository ingredientRepository, ExternalRecipeToWebInputMapper externalRecipeToWebInputMapper) {
		this.recipeParser = recipeParser;
		this.ingredientRepository = ingredientRepository;
		this.externalRecipeToWebInputMapper = externalRecipeToWebInputMapper;
	}

	@Override
	public RecipeWebInput importRecipe(String urlString) throws ImporterServiceException {
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

	private void checkNullParameter(Object parameter) throws ImporterServiceException {
		if (parameter == null) throw new ImporterServiceException("Parameter must not be empty!");
	}
}
