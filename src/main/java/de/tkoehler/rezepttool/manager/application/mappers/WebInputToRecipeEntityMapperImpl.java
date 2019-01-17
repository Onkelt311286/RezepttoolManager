package de.tkoehler.rezepttool.manager.application.mappers;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.web.model.IngredientWebInputCreate;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInputCreate;

@Component
public class WebInputToRecipeEntityMapperImpl implements WebInputToRecipeEntityMapper {

	@Override
	public RecipeEntity process(RecipeWebInputCreate webRecipe) {
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
				.build();
		result.setDifficulty(webRecipe.getDifficulty());
		for (IngredientWebInputCreate ingredient : webRecipe.getIngredients()) {
			RecipeIngredient recipeIngredient = RecipeIngredient.builder()
					.id(UUID.randomUUID().toString())
					.amount(ingredient.getAmount())
					.recipe(result)
					.ingredient(Ingredient.builder()
							.id(UUID.randomUUID().toString())
							.name(ingredient.getName())
							.alternativeNames(Stream.of(
									ingredient.getOriginalName().equals("") ? ingredient.getName() : ingredient.getOriginalName() 
											).collect(Collectors.toSet()))
							.department(ingredient.getDepartment())
							.build())
					.build();
			result.addRecipeIngredient(recipeIngredient);
		}
		return result;
	}
}
