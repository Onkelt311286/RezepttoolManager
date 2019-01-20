package de.tkoehler.rezepttool.manager.services;

import java.util.List;

import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

public interface ManagerService {

	List<TinyRecipe> showRecipeList();

	List<TinyRecipe> filterRecipeList() throws ManagerServiceException;

	RecipeWebInput verifyRecipe(RecipeWebInput recipe) throws ManagerServiceException;

	void saveRecipe(RecipeWebInput recipe) throws ManagerServiceException;

	RecipeWebInput editRecipe(String recipeId) throws ManagerServiceException;

	void deleteRecipe(String recipeId) throws ManagerServiceException;
}