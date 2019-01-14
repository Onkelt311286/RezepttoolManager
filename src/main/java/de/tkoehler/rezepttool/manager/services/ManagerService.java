package de.tkoehler.rezepttool.manager.services;

import java.util.List;

import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;

public interface ManagerService {

	List<RecipeEntity> showRecipeList() throws ManagerServiceException;

	RecipeEntity presentRecipe(String recipeId) throws ManagerServiceException;

	void editRecipe(RecipeEntity recipe) throws ManagerServiceException;

	void deleteRecipe(String recipeId) throws ManagerServiceException;

}
