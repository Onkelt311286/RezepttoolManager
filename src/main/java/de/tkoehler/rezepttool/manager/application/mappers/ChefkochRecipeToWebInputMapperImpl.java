package de.tkoehler.rezepttool.manager.application.mappers;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.services.model.ChefkochIngredient;
import de.tkoehler.rezepttool.manager.services.model.ChefkochRecipe;
import de.tkoehler.rezepttool.manager.services.model.Recipe;
import de.tkoehler.rezepttool.manager.web.model.IngredientWebInputCreate;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInputCreate;

@Component
public class ChefkochRecipeToWebInputMapperImpl implements ExternalRecipeToWebInputMapper {

	@Override
	public RecipeWebInputCreate process(Recipe recipe) {
		if (recipe == null) return null;
		ChefkochRecipe ckRecipe = (ChefkochRecipe) recipe;
		RecipeWebInputCreate result = RecipeWebInputCreate.builder()
				.url(ckRecipe.getUrl())
				.name(ckRecipe.getName())
				.additionalInformation(ckRecipe.getPrintPageData().getAdditionalInformation())
				.portions(ckRecipe.getRecipeYield())
				.instructions(ckRecipe.getPrintPageData().getInstructions())
				.workTime(ckRecipe.getPreparationInfo().getPrepTime())
				.cookTime(ckRecipe.getPreparationInfo().getCookTime())
				.restTime(ckRecipe.getPreparationInfo().getRestTime())
				.callories(ckRecipe.getPreparationInfo().getCallories())
				.categories(Arrays.asList(ckRecipe.getRecipeCategories()).stream().map(cat -> cat = cat.replace("\"", "")).collect(Collectors.toList()))
				.build();
		result.setDifficulty(ckRecipe.getPreparationInfo().getDifficulty());
		for (ChefkochIngredient ingredient : ckRecipe.getPrintPageData().getIngredients()) {
			IngredientWebInputCreate recipeIngredient = IngredientWebInputCreate.builder()
					.amount(ingredient.getAmount())
					.name(ingredient.getName())
					.originalName(ingredient.getName())
					.department("")
					.build();
			result.getIngredients().add(recipeIngredient);
		}
		return result;
	}
}
