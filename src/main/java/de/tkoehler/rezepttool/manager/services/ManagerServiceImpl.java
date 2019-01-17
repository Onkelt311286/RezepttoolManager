package de.tkoehler.rezepttool.manager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.tkoehler.rezepttool.manager.application.mappers.RecipeEntityToWebInputMapper;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInputEdit;
import lombok.extern.slf4j.Slf4j;

@Component
@Transactional
@Slf4j
public class ManagerServiceImpl implements ManagerService {

	private final RecipeRepository recipeRepository;
	private final RecipeEntityToWebInputMapper recipeEntityToWebInputMapper;

	public ManagerServiceImpl(RecipeRepository recipeRepository, RecipeEntityToWebInputMapper recipeEntityToWebInputMapper) {
		this.recipeRepository = recipeRepository;
		this.recipeEntityToWebInputMapper = recipeEntityToWebInputMapper;
	}

	@Override
	public List<TinyRecipe> showRecipeList() throws ManagerServiceException {
		return recipeRepository.findAllTinies();
	}

	@Override
	public RecipeWebInputEdit editRecipe(String recipeId) throws ManagerServiceException {
		Optional<RecipeEntity> recipe = recipeRepository.findById(recipeId);
		if (recipe.isPresent()) {
			
			RecipeEntity recipeEntity = recipe.get();
			log.info(recipeEntity.getName() + " " + recipeEntity.getDifficulty());
			return recipeEntityToWebInputMapper.process(recipe.get());
		}
		else throw new ManagerServiceException("Zur übergebenen ID konnte kein Rezept gefunden werden!");
	}

	@Override
	public void deleteRecipe(String recipeId) throws ManagerServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveRecipe(RecipeWebInputEdit recipe) throws ManagerServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public RecipeWebInputEdit verifyRecipe(RecipeWebInputEdit recipe) throws ManagerServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TinyRecipe> filterRecipeList() throws ManagerServiceException {
		List<TinyRecipe> recipes = recipeRepository.findAllTiniesByCategory("\"fettarm\"");
		log.info("Filtersize: " + recipes.size());
		for (TinyRecipe tinyRecipe : recipes) {
			log.info(tinyRecipe.getName());
		}
		return null;
	}
}
