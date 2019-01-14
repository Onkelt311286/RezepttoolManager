package de.tkoehler.rezepttool.manager.services;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import lombok.extern.slf4j.Slf4j;

@Component
@Transactional
@Slf4j
public class ManagerServiceImpl implements ManagerService {

	private final RecipeRepository recipeRepository;

	public ManagerServiceImpl(RecipeRepository recipeRepository) {
		super();
		this.recipeRepository = recipeRepository;
	}

	@Override
	public List<RecipeEntity> showRecipeList() throws ManagerServiceException {
		return recipeRepository.findAll();
	}

	@Override
	public RecipeEntity presentRecipe(String recipeId) throws ManagerServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void editRecipe(RecipeEntity recipe) throws ManagerServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteRecipe(String recipeId) throws ManagerServiceException {
		// TODO Auto-generated method stub

	}

}
