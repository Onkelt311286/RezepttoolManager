package de.tkoehler.rezepttool.manager.application.mappers;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.web.model.IngredientWebInputEdit;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInputEdit;

@Component
public class RecipeEntityToWebInputMapperImpl implements RecipeEntityToWebInputMapper {

	@Override
	public RecipeWebInputEdit process(RecipeEntity recipe) {
		if (recipe == null) return null;
		RecipeWebInputEdit result = RecipeWebInputEdit.builder()
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
			IngredientWebInputEdit editIngredient = IngredientWebInputEdit.builder()
					.amount(ingredient.getAmount())
					.name(ingredient.getIngredient().getName())
					.department(ingredient.getIngredient().getDepartment())
					.build();
			result.getIngredients().add(editIngredient);
		}
		return result;
	}

}
