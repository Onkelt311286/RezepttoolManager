package de.tkoehler.rezepttool.manager.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.FilterableRecipe;
import de.tkoehler.rezepttool.manager.repositories.model.TinyIngredient;
import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;

@Component
public class ManagerServiceImpl implements ManagerService {
	private final RecipeRepository recipeRepository;
	private final IngredientRepository ingredientRepository;

	public ManagerServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
		this.recipeRepository = recipeRepository;
		this.ingredientRepository = ingredientRepository;
	}

	@Override
	public List<TinyRecipe> findAllTinyRecipes() {
		return recipeRepository.findAllTinies();
	}

	@Override
	public List<TinyIngredient> findAllTinyIngredients() {
		return ingredientRepository.findAllTinies();
	}

	@Override
	public List<TinyIngredient> findAllTiniesByNameAndDepartment(String name, String department) {
		return ingredientRepository.findAllTiniesByNameAndDepartment(name, department);
	}

	@Override
	public List<TinyIngredient> findAllTiniesByName(String name) {
		return ingredientRepository.findAllTiniesByName(name);
	}

	@Override
	public List<TinyIngredient> findAllTiniesByDepartment(String department) {
		return ingredientRepository.findAllTiniesByDepartment(department);
	}

	@Override
	public List<TinyIngredient> findIngredientNamesByName(String name) {
		List<TinyIngredient> result = new ArrayList<>();
		ingredientRepository.findIngredientNamesByName(name).stream().forEach(n -> result.add(TinyIngredient.builder().name(n).department(n).build()));
		return result;
	}

	@Override
	public List<TinyIngredient> findDepartmentsByName(String department) {
		List<TinyIngredient> result = new ArrayList<>();
		ingredientRepository.findDepartmentsByName(department).stream().forEach(n -> result.add(TinyIngredient.builder().name(n).department(n).build()));
		return result;
	}

	@Override
	public List<FilterableRecipe> findAllFilterableRecipes() {
		List<FilterableRecipe> result = new ArrayList<>();
		recipeRepository.findAllTinies().stream().forEach(recipe -> {
			List<String> categories = recipeRepository.findCategoriesById(recipe.getId());
			List<String> ingredientNames = recipeRepository.findIngredientsById(recipe.getId());
			result.add(FilterableRecipe.builder()
					.id(recipe.getId())
					.name(recipe.getName())
					.categories(categories)
					.ingredients(ingredientNames)
					.build());
		});
		return result;
	}
}
