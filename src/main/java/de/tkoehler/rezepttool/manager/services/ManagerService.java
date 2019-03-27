package de.tkoehler.rezepttool.manager.services;

import java.util.List;

import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

public interface ManagerService {

	List<TinyRecipe> findAllTinies();

	void updateRecipe(RecipeWebInput updatedRecipe) throws ManagerServiceException;

	void deleteRecipe(String recipeId) throws ManagerServiceException;

	RecipeWebInput loadRecipe(String id) throws ManagerServiceException;
}