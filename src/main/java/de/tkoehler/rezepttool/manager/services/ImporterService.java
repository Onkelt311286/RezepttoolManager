package de.tkoehler.rezepttool.manager.services;

import java.util.List;

import de.tkoehler.rezepttool.manager.repositories.model.TinyIngredient;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

public interface ImporterService {
	RecipeWebInput loadRecipe(String urlString) throws ImporterServiceException;
	void importRecipe(RecipeWebInput newRecipe) throws ImporterServiceException;
	List<TinyIngredient> findAllTinies();
	List<TinyIngredient> findAllTiniesByNameAndDepartment(String name, String department);
	List<TinyIngredient> findAllTiniesByName(String name);
	List<TinyIngredient> findAllTiniesByDepartment(String department);
	List<TinyIngredient> findIngredientNamesByName(String name);
	List<TinyIngredient> findDepartmentsByName(String department);
}
