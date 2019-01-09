package de.tkoehler.rezepttool.manager.application.mappers;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.tkoehler.rezepttool.manager.services.model.ChefkochIngredient;
import de.tkoehler.rezepttool.manager.services.model.ChefkochRecipe;
import de.tkoehler.rezepttool.manager.services.model.Recipe;
import de.tkoehler.rezepttool.manager.web.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

public class ChefkochRecipeToWebInputMapperImpl implements ExternalRecipeToWebInputMapper {

	@Override
	public RecipeWebInput process(Recipe recipe) {
		if (recipe == null) return null;
		ChefkochRecipe ckRecipe = (ChefkochRecipe) recipe;
		RecipeWebInput result = RecipeWebInput.builder()
				.url(ckRecipe.getUrl())
				.name(ckRecipe.getName())
				.additionalInformation(ckRecipe.getPrintPageData().getAdditionalInformation())
				.portions(ckRecipe.getRecipeYield())
				.instructions(ckRecipe.getPrintPageData().getInstructions())
				.workTime(ckRecipe.getPreparationInfo().getPrepTime())
				.cookTime(ckRecipe.getPreparationInfo().getCookTime())
				.restTime(ckRecipe.getPreparationInfo().getRestTime())
				.callories(ckRecipe.getPreparationInfo().getCallories())
				.categories(Stream.of(ckRecipe.getRecipeCategories()).collect(Collectors.toSet()))
				.build();
		result.setDifficulty(ckRecipe.getPreparationInfo().getDifficulty());
		for (ChefkochIngredient ingredient : ckRecipe.getPrintPageData().getIngredients()) {
			IngredientWebInput recipeIngredient = IngredientWebInput.builder()
					.amount(ingredient.getAmount())
					.name(ingredient.getName())
					.originalName(ingredient.getName())
					.build();
			result.getIngredients().add(recipeIngredient);
		}
		return result;
	}
}
