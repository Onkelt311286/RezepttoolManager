package de.tkoehler.rezepttool.manager.application.mappers;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.restcontroller.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;

@Component
public class WebInputToRecipeEntityMapperImpl implements WebInputToRecipeEntityMapper {

	@Override
	public RecipeEntity process(RecipeWebInput webRecipe) {
		if (webRecipe == null) return null;
		RecipeEntity result = RecipeEntity.builder()
				.id(getOrCreateId(webRecipe.getId()))
//				.id(webRecipe.getId().equals("") ? UUID.randomUUID().toString() : webRecipe.getId())
				.url(webRecipe.getUrl())
				.name(webRecipe.getName())
				.additionalInformation(webRecipe.getAdditionalInformation())
				.portions(webRecipe.getPortions())
				.instructions(webRecipe.getInstructions())
				.workTime(webRecipe.getWorkTime())
				.cookTime(webRecipe.getCookTime())
				.restTime(webRecipe.getRestTime())
				.callories(webRecipe.getCallories())
				.categories(new HashSet<>(webRecipe.getCategories()))
				.build();
		result.setDifficulty(webRecipe.getDifficulty());
		for (IngredientWebInput ingredient : webRecipe.getIngredients()) {
			RecipeIngredient recipeIngredient = RecipeIngredient.builder()
					.id(getOrCreateId(ingredient.getRecipeIngredientId()))
//					.id(ingredient.getRecipeIngredientId().equals("") ? UUID.randomUUID().toString() : ingredient.getRecipeIngredientId())
					.amount(ingredient.getAmount())
					.recipe(result)
					.ingredient(Ingredient.builder()
							.id(getOrCreateId(ingredient.getIngredientId()))
//							.id(ingredient.getIngredientId().equals("") ? UUID.randomUUID().toString() : ingredient.getIngredientId())
							.name(ingredient.getName())
							.alternativeNames(Stream.of(
									ingredient.getOriginalName().equals("") ? ingredient.getName() : ingredient.getOriginalName()).collect(Collectors.toSet()))
							.department(ingredient.getDepartment())
							.build())
					.build();
			result.addRecipeIngredient(recipeIngredient);
		}
		return result;
	}

	private String getOrCreateId(String id) {
		if(id == null || id.equals(""))
			return UUID.randomUUID().toString();
		return id;
	}
}
