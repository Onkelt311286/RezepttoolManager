package de.tkoehler.rezepttool.manager.application.mappers;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.restcontroller.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;
import de.tkoehler.rezepttool.manager.services.model.ChefkochIngredient;
import de.tkoehler.rezepttool.manager.services.model.ChefkochRecipe;
import de.tkoehler.rezepttool.manager.services.model.Recipe;

@Component
@Primary
@Qualifier("Chefkoch")
public class ChefkochRecipeToWebInputMapperImpl implements ExternalRecipeToWebInputMapper {

	@Override
	public RecipeWebInput process(Recipe recipe) {
		if (recipe == null) return null;
		ChefkochRecipe ckRecipe = (ChefkochRecipe) recipe;
		RecipeWebInput result = RecipeWebInput.builder()
				.id("")
				.url(ckRecipe.getUrl())
				.name(ckRecipe.getName())
				.additionalInformation(ckRecipe.getPrintPageData().getAdditionalInformation())
				.portions(ckRecipe.getRecipeYield())
				.instructions(ckRecipe.getRecipeInstructions())
				.workTime(ckRecipe.getPreparationInfo().getPrepTime())
				.cookTime(ckRecipe.getPreparationInfo().getCookTime())
				.restTime(ckRecipe.getPreparationInfo().getRestTime())
				.callories(ckRecipe.getPreparationInfo().getCallories())
				.categories(Arrays.asList(ckRecipe.getRecipeCategories()).stream().map(cat -> cat = cat.replace("\"", "")).collect(Collectors.toList()))
				.build();
		result.setDifficulty(ckRecipe.getPreparationInfo().getDifficulty());
		for (ChefkochIngredient ingredient : ckRecipe.getPrintPageData().getIngredients()) {
			IngredientWebInput recipeIngredient = IngredientWebInput.builder()
					.recipeIngredientId("")
					.ingredientId("")
					.amount(ingredient.getAmount())
					.name(ingredient.getName())
					.originalName(ingredient.getName())
					.department("")
					.originalDepartment("")
					.unequalToEntity(false)
					.build();
			result.getIngredients().add(recipeIngredient);
		}
		return result;
	}
}
