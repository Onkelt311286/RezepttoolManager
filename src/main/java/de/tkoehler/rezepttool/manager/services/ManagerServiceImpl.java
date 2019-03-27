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
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;
import lombok.extern.slf4j.Slf4j;

@Component
@Transactional
@Slf4j
public class ManagerServiceImpl implements ManagerService {
	private final RecipeRepository recipeRepository;
	private final IngredientRepository ingredientRepository;
	private final RecipeEntityToWebInputMapper recipeEntityToWebInputMapper;
	private final WebInputToRecipeEntityMapper webInputToRecipeEntityMapper;

	public ManagerServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository, RecipeEntityToWebInputMapper recipeEntityToWebInputMapper,
			WebInputToRecipeEntityMapper webInputToRecipeEntityMapper) {
		this.recipeRepository = recipeRepository;
		this.ingredientRepository = ingredientRepository;
		this.recipeEntityToWebInputMapper = recipeEntityToWebInputMapper;
		this.webInputToRecipeEntityMapper = webInputToRecipeEntityMapper;
	}

	@Override
	public List<TinyRecipe> findAllTinies() {
		return recipeRepository.findAllTinies();
	}

	@Override
	public RecipeWebInput loadRecipe(String id) throws ManagerServiceException {
		checkNullParameter(id);
		Optional<RecipeEntity> result = recipeRepository.findById(id);
		if (result.isPresent()) return recipeEntityToWebInputMapper.process(result.get());
		else throw new ManagerServiceIDNotFoundException("ID could not be found!");
	}
	
	@Override
	public void updateRecipe(RecipeWebInput updatedRecipe) throws ManagerServiceException {
		checkNullParameter(updatedRecipe);
		Optional<RecipeEntity> oldRecipe = recipeRepository.findById(updatedRecipe.getId());
		if (oldRecipe.isPresent()) {
			RecipeEntity newRecipe = webInputToRecipeEntityMapper.process(updatedRecipe);
			newRecipe.getIngredients().stream().forEach(i -> updateKnownIngredient(i.getIngredient()));
			recipeRepository.save(newRecipe);
		}
		else throw new ManagerServiceIDNotFoundException("ID could not be found!");
	}
	
	public void updateKnownIngredient(Ingredient ingredient) {
		Optional<Ingredient> ingredientEntity = ingredientRepository.findByNameAndDepartment(ingredient.getName(), ingredient.getDepartment());
		if (ingredientEntity.isPresent()) {
			ingredient.setId(ingredientEntity.get().getId());
			ingredient.getAlternativeNames().addAll(ingredientEntity.get().getAlternativeNames());
		}
	}
	
	@Override
	public void deleteRecipe(String recipeId) throws ManagerServiceException {
		checkNullParameter(recipeId);
		recipeRepository.deleteById(recipeId);
	}

	private void checkNullParameter(Object parameter) throws ManagerServiceException {
		if (parameter == null) throw new ManagerServiceException("Parameter must not be empty!");
	}
}
