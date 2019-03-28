package de.tkoehler.rezepttool.manager.services;

import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;

public interface EditorService {

	RecipeWebInput loadRecipe(String id) throws EditorServiceException;

	void insertRecipe(RecipeWebInput newRecipe) throws EditorServiceException;

	void updateRecipe(RecipeWebInput updatedRecipe) throws EditorServiceException;

	void deleteRecipe(String recipeId) throws EditorServiceException;
}
