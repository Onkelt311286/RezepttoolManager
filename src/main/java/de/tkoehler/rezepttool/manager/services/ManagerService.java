package de.tkoehler.rezepttool.manager.services;

import java.util.List;

import de.tkoehler.rezepttool.manager.repositories.model.TinyIngredient;
import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;

public interface ManagerService {

	List<TinyRecipe> findAllTinyRecipes();

	List<TinyIngredient> findAllTinyIngredients();

	List<TinyIngredient> findAllTiniesByNameAndDepartment(String name, String department);

	List<TinyIngredient> findAllTiniesByName(String name);

	List<TinyIngredient> findAllTiniesByDepartment(String department);

	List<TinyIngredient> findIngredientNamesByName(String name);

	List<TinyIngredient> findDepartmentsByName(String department);

}