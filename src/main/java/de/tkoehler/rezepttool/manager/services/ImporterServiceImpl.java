package de.tkoehler.rezepttool.manager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.esotericsoftware.minlog.Log;

import de.tkoehler.rezepttool.manager.application.mappers.ServiceRecipeToRepoRecipeMapper;
import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.Recipe;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParser;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParserException;

@Component
@Transactional
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

	// TODO: irgendiwe ist das Verhalten nicht gut lesbar, mit den vielen Annahmen
	// ... Das Funktioniert halt, weil ich das Objekt auf eine ganz bestimmte Art
	// gebaut habe. Sollte so umgebaut werden, dass es auch noch funtkioniert, wenn
	// man das Objekt auf eine andere Art baut.
	public void updateKnownIngredients(List<RecipeIngredient> recipeIngredients) throws ImporterServiceException {
		checkNullParameter(recipeIngredients);
		for (RecipeIngredient recipeIngredient : recipeIngredients) {
			if (recipeIngredient.getIngredient().getName().equals("")) {
				for (String alternativeName : recipeIngredient.getIngredient().getAlternativeNames()) {
					Log.info(alternativeName);
					List<Ingredient> ingredients = ingredientRepository.findByAlternativeName(alternativeName);
					if (ingredients.size() == 0) recipeIngredient.getIngredient().setName(alternativeName);
					else {
						updateIngredient(recipeIngredient, ingredients.get(0));
					}
				}
			}
			else {
				List<Ingredient> ingredients = ingredientRepository.findByName(recipeIngredient.getIngredient().getName());
				if (ingredients.size() == 0) return;
				else {
//					Optional<String> newAlternativeName = recipeIngredient.getIngredient().getAlternativeNames().stream().findFirst();
//					updateIngredient(recipeIngredient, ingredients.get(0));
//					recipeIngredient.getIngredient().getAlternativeNames().add(newAlternativeName);
				}
			}
		}
	}

	private void updateIngredient(RecipeIngredient recipeIngredient, Ingredient ingredient) {
		recipeIngredient.setIngredient(ingredient);
		recipeIngredient.getIngredient().addRecipeIngredient(recipeIngredient);
	}

	@Override
	public void saveRecipe(Recipe recipe) throws ImporterServiceException {
		checkNullParameter(recipe);
		updateKnownIngredients(recipe.getIngredients());
		recipeRepository.save(recipe);
	}

	private void checkNullParameter(Object parameter) throws ImporterServiceException {
		if (parameter == null)
			throw new ImporterServiceException("Parameter must not be empty!");
	}
}
