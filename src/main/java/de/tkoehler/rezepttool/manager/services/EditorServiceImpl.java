package de.tkoehler.rezepttool.manager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.tkoehler.rezepttool.manager.application.mappers.RecipeEntityToWebInputMapper;
import de.tkoehler.rezepttool.manager.application.mappers.WebInputToRecipeEntityMapper;
import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;

@Component
@Transactional
public class EditorServiceImpl implements EditorService {
	
	private final RecipeRepository recipeRepository;
	private final IngredientRepository ingredientRepository;
	private final RecipeEntityToWebInputMapper recipeEntityToWebInputMapper;
	private final WebInputToRecipeEntityMapper webInputToRecipeEntityMapper;

	public EditorServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, RecipeEntityToWebInputMapper recipeEntityToWebInputMapper,
			WebInputToRecipeEntityMapper webInputToRecipeEntityMapper) {
		this.recipeRepository = recipeRepository;
		this.ingredientRepository = ingredientRepository;
		this.recipeEntityToWebInputMapper = recipeEntityToWebInputMapper;
		this.webInputToRecipeEntityMapper = webInputToRecipeEntityMapper;
	}

	@Override
	public RecipeWebInput loadRecipe(String id) throws EditorServiceException {
		checkNullParameter(id);
		Optional<RecipeEntity> result = recipeRepository.findById(id);
		if (result.isPresent()) return recipeEntityToWebInputMapper.process(result.get());
		else throw new EditorServiceIDNotFoundException("ID could not be found!");
	}

	@Override
	public void insertRecipe(RecipeWebInput newRecipe) throws EditorServiceException {
		checkNullParameter(newRecipe);
		checkForExistingRecipe(newRecipe);
		RecipeEntity recipe = webInputToRecipeEntityMapper.process(newRecipe);
		recipe.getIngredients().stream().forEach(i -> updateKnownIngredient(i.getIngredient()));
		recipeRepository.save(recipe);
	}

	@Override
	public void updateRecipe(RecipeWebInput updatedRecipe) throws EditorServiceException {
		checkNullParameter(updatedRecipe);
		Optional<RecipeEntity> oldRecipe = recipeRepository.findById(updatedRecipe.getId());
		if (oldRecipe.isPresent()) {
			RecipeEntity newRecipe = webInputToRecipeEntityMapper.process(updatedRecipe);
			newRecipe.getIngredients().stream().forEach(i -> updateKnownIngredient(i.getIngredient()));
			recipeRepository.save(newRecipe);
		}
		else throw new EditorServiceIDNotFoundException("ID could not be found!");
	}

	@Override
	public void deleteRecipe(String recipeId) throws EditorServiceException {
		checkNullParameter(recipeId);
		recipeRepository.deleteById(recipeId);
	}
	
	private void updateKnownIngredient(Ingredient ingredient) {
		Optional<Ingredient> ingredientEntity = ingredientRepository.findByNameAndDepartment(ingredient.getName(), ingredient.getDepartment());
		if (ingredientEntity.isPresent()) {
			ingredient.setId(ingredientEntity.get().getId());
			ingredient.getAlternativeNames().addAll(ingredientEntity.get().getAlternativeNames());
		}
	}
	
	private void checkForExistingRecipe(RecipeWebInput recipe) throws EditorServiceRecipeExistsException {
		List<RecipeEntity> recipes = recipeRepository.findByUrlAndName(recipe.getUrl(), recipe.getName());
		if (recipes.size() > 0)
			throw new EditorServiceRecipeExistsException("Recipe already exists!");
	}
	
	private void checkNullParameter(Object parameter) throws EditorServiceException {
		if (parameter == null) throw new EditorServiceException("Parameter must not be empty!");
	}
}
