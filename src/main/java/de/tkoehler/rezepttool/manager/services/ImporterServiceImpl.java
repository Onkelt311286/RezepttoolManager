package de.tkoehler.rezepttool.manager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.application.mappers.ExternalRecipeToWebInputMapper;
import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.restcontroller.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;
import de.tkoehler.rezepttool.manager.services.exceptions.ImporterServiceException;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParser;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParserException;

@Component
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
	public RecipeWebInput loadRecipeFromExternal(String urlString) throws ImporterServiceException {
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

	private void updateWebRecipeWithKnownData(RecipeWebInput recipe) throws ImporterServiceException {
		checkNullParameter(recipe);
		for (IngredientWebInput ingredient : recipe.getIngredients()) {
			updateWebIngredientWithKnownData(ingredient);
		}
	}

	private void updateWebIngredientWithKnownData(IngredientWebInput ingredient) throws ImporterServiceException {
		List<Ingredient> ingredients = ingredientRepository.findByAlternativeName(ingredient.getOriginalName());
		Optional<Ingredient> multiNameIngredient = ingredients.stream().reduce((i1, i2) -> reduceToIngredientWithAllNames(i1, i2));
		if (multiNameIngredient.isPresent()) {
			ingredient.setDepartment(multiNameIngredient.get().getDepartment());
			ingredient.setName(multiNameIngredient.get().getName());
		}
	}

	private Ingredient reduceToIngredientWithAllNames(Ingredient ingredient1, Ingredient ingredient2) {
		String newName = ingredient1.getName();
		if (!newName.contains(ingredient2.getName()))
			newName += " | " + ingredient2.getName();
		String newDepartment = ingredient1.getDepartment();
		if (!newDepartment.contains(ingredient2.getDepartment()))
			newDepartment += " | " + ingredient2.getDepartment();
		return Ingredient.builder().name(newName).department(newDepartment).build();
	}

	private void checkNullParameter(Object parameter) throws ImporterServiceException {
		if (parameter == null) throw new ImporterServiceException("Parameter must not be empty!");
	}
}
