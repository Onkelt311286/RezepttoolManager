package de.tkoehler.rezepttool.manager.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.tkoehler.rezepttool.manager.application.mappers.ExternalRecipeToWebInputMapper;
import de.tkoehler.rezepttool.manager.application.mappers.WebInputToRecipeEntityMapper;
import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.repositories.model.TinyIngredient;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParser;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParserException;
import de.tkoehler.rezepttool.manager.web.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@Component
@Transactional
public class ImporterServiceImpl implements ImporterService {

	private final RecipeParser recipeParser;
	private final RecipeRepository recipeRepository;
	private final IngredientRepository ingredientRepository;
	private final ExternalRecipeToWebInputMapper externalRecipeToWebInputMapper;
	private final WebInputToRecipeEntityMapper webInputToRecipeEntityMapper;

	public ImporterServiceImpl(RecipeParser recipeParser, RecipeRepository recipeRepository, IngredientRepository ingredientRepository, ExternalRecipeToWebInputMapper externalRecipeToWebInputMapper,
			WebInputToRecipeEntityMapper webInputToRecipeEntityMapper) {
		super();
		this.recipeParser = recipeParser;
		this.recipeRepository = recipeRepository;
		this.ingredientRepository = ingredientRepository;
		this.externalRecipeToWebInputMapper = externalRecipeToWebInputMapper;
		this.webInputToRecipeEntityMapper = webInputToRecipeEntityMapper;
	}

	@Override
	public RecipeWebInput loadRecipe(String urlString) throws ImporterServiceException {
		checkNullParameter(urlString);
		try {
			RecipeWebInput recipe = externalRecipeToWebInputMapper.process(recipeParser.parseRecipe(urlString));
			updateWebRecipeWithKnownData(recipe);
			return recipe;
		}
		catch (RecipeParserException e) {
			throw new ImporterServiceException("Failed to parse recipe!", e);
		}
	}

	public void updateWebRecipeWithKnownData(RecipeWebInput recipe) throws ImporterServiceException {
		checkNullParameter(recipe);
		for (IngredientWebInput ingredient : recipe.getIngredients()) {
			updateWebIngredientWithKnownData(ingredient);
		}
	}

	public void updateWebIngredientWithKnownData(IngredientWebInput ingredient) throws ImporterServiceException {
		checkNullParameter(ingredient);
		List<Ingredient> ingredients = ingredientRepository.findByAlternativeName(ingredient.getOriginalName());
		Optional<Ingredient> multiNameIngredient = ingredients.stream().reduce((i1, i2) -> reduceToIngredientWithAllNames(i1, i2));
		if (multiNameIngredient.isPresent()) {
			ingredient.setDepartment(multiNameIngredient.get().getDepartment());
			ingredient.setName(multiNameIngredient.get().getName());
		}
	}

	public Ingredient reduceToIngredientWithAllNames(Ingredient ingredient1, Ingredient ingredient2) {
		String newName = ingredient1.getName();
		if (!newName.contains(ingredient2.getName()))
			newName += " | " + ingredient2.getName();
		String newDepartment = ingredient1.getDepartment();
		if (!newDepartment.contains(ingredient2.getDepartment()))
			newDepartment += " | " + ingredient2.getDepartment();
		return Ingredient.builder().name(newName).department(newDepartment).build();
	}

	@Override
	public void importRecipe(RecipeWebInput newRecipe) throws ImporterServiceException {
		checkNullParameter(newRecipe);
		checkForExistingRecipe(newRecipe);
		RecipeEntity recipe = webInputToRecipeEntityMapper.process(newRecipe);
		recipe.getIngredients().stream().forEach(i -> updateKnownIngredient(i.getIngredient()));
		recipeRepository.save(recipe);
	}

	public void updateKnownIngredient(Ingredient ingredient) {
		Optional<Ingredient> ingredientEntity = ingredientRepository.findByNameAndDepartment(ingredient.getName(), ingredient.getDepartment());
		if (ingredientEntity.isPresent()) {
			ingredient.setId(ingredientEntity.get().getId());
			ingredient.getAlternativeNames().addAll(ingredientEntity.get().getAlternativeNames());
		}
	}

	private void checkForExistingRecipe(RecipeWebInput recipe) throws ImporterServiceRecipeExistsException {
		List<RecipeEntity> recipes = recipeRepository.findByUrlAndName(recipe.getUrl(), recipe.getName());
		if (recipes.size() > 0)
			throw new ImporterServiceRecipeExistsException("Recipe already exists!");
	}

	private void checkNullParameter(Object parameter) throws ImporterServiceException {
		if (parameter == null) throw new ImporterServiceException("Parameter must not be empty!");
	}

	@Override
	public List<TinyIngredient> findAllTinies() {
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
}
