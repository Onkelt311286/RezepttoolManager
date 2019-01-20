package de.tkoehler.rezepttool.manager.application.mappers;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.web.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@Component
public class RecipeEntityToWebInputMapperImpl implements RecipeEntityToWebInputMapper {

	@Override
	public RecipeWebInput process(RecipeEntity recipe) {
		if (recipe == null) return null;
		RecipeWebInput result = RecipeWebInput.builder()
				.id(recipe.getId())
				.url(recipe.getUrl())
				.name(recipe.getName())
				.additionalInformation(recipe.getAdditionalInformation())
				.portions(recipe.getPortions())
				.instructions(recipe.getInstructions())
				.workTime(recipe.getWorkTime())
				.cookTime(recipe.getCookTime())
				.restTime(recipe.getRestTime())
				.callories(recipe.getCallories())
				.categories(new ArrayList<>(recipe.getCategories()))
				.build();
		result.setDifficulty(recipe.getDifficulty().toString().toLowerCase());
		for (RecipeIngredient ingredient : recipe.getIngredients()) {
			IngredientWebInput editIngredient = IngredientWebInput.builder()
					.recipeIngredientId(ingredient.getId())
					.amount(ingredient.getAmount())
					.ingredientId(ingredient.getIngredient().getId())
					.name(ingredient.getIngredient().getName())
					.originalName(ingredient.getIngredient().getName())
					.department(ingredient.getIngredient().getDepartment())
					.originalDepartment(ingredient.getIngredient().getDepartment())
					.unequalToEntity(false)
					.build();
			result.getIngredients().add(editIngredient);
		}
		return result;
	}

}
