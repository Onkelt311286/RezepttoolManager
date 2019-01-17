package de.tkoehler.rezepttool.manager.services;

import java.util.List;

import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInputEdit;

public interface ManagerService {

	List<TinyRecipe> showRecipeList() throws ManagerServiceException;
	
	List<TinyRecipe> filterRecipeList() throws ManagerServiceException;

	RecipeWebInputEdit editRecipe(String recipeId) throws ManagerServiceException;

	void saveRecipe(RecipeWebInputEdit recipe) throws ManagerServiceException;

	RecipeWebInputEdit verifyRecipe(RecipeWebInputEdit recipe) throws ManagerServiceException;

	void deleteRecipe(String recipeId) throws ManagerServiceException;

}
