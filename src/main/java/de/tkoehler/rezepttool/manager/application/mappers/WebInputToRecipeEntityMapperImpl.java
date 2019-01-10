package de.tkoehler.rezepttool.manager.application.mappers;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.web.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@Component
public class WebInputToRecipeEntityMapperImpl implements WebInputToRecipeEntityMapper {

	@Override
	public RecipeEntity process(RecipeWebInput webRecipe) {
		if (webRecipe == null) return null;
		RecipeEntity result = RecipeEntity.builder()
				.id(UUID.randomUUID().toString())
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
				.difficulty(webRecipe.getDifficulty())
				.build();
		for (IngredientWebInput ingredient : webRecipe.getIngredients()) {
			RecipeIngredient recipeIngredient = RecipeIngredient.builder()
					.id(UUID.randomUUID().toString())
					.amount(ingredient.getAmount())
					.recipe(result)
					.ingredient(Ingredient.builder()
							.id(UUID.randomUUID().toString())
							.name(ingredient.getName())
							.alternativeNames(Stream.of(ingredient.getOriginalName()).collect(Collectors.toSet()))
							.build())
					.build();
			result.addRecipeIngredient(recipeIngredient);
			recipeIngredient.getIngredient().addRecipeIngredient(recipeIngredient);
		}
		return result;
	}
}
