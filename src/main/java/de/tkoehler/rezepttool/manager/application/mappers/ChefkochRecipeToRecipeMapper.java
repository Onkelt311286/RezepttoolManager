package de.tkoehler.rezepttool.manager.application.mappers;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.repositories.model.AlternativeIngredientName;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.Recipe;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.services.model.ChefkochIngredient;
import de.tkoehler.rezepttool.manager.services.model.ChefkochRecipe;

@Component
public class ChefkochRecipeToRecipeMapper implements ServiceRecipeToRepoRecipeMapper {

	@Override
	public Recipe process(de.tkoehler.rezepttool.manager.services.model.Recipe serviceRecipe) {
		if (serviceRecipe == null) return null;
		ChefkochRecipe ckRecipe = (ChefkochRecipe) serviceRecipe;
		Recipe result = new Recipe();
		result.setId(UUID.randomUUID().toString());
		result.setUrl(ckRecipe.getUrl());
		result.setName(ckRecipe.getName());
		result.setAdditionalInformationen(ckRecipe.getPrintPageData().getAdditionalInformation());
		result.setPortions(ckRecipe.getRecipeYield());

		for (ChefkochIngredient ckIngredient : ckRecipe.getPrintPageData().getIngredients()) {
			RecipeIngredient recipeIngredient = RecipeIngredient.builder()
					.id(UUID.randomUUID().toString())
					.amount(ckIngredient.getAmount())
					.recipe(result)
					.ingredient(Ingredient.builder()
							.id(UUID.randomUUID().toString())
							.name("")
							.build())
					.build();
			result.addRecipeIngredient(recipeIngredient);
			recipeIngredient.getIngredient().addIngredient(recipeIngredient);
			recipeIngredient.getIngredient().addAlternativeName(AlternativeIngredientName.builder()
					.id(UUID.randomUUID().toString())
					.name(ckIngredient.getName())
					.ingredient(recipeIngredient.getIngredient())
					.build());
		}

		result.setInstructions(ckRecipe.getPrintPageData().getInstructions());
		result.setWorkTime(ckRecipe.getPreparationInfo().getPrepTime());
		result.setCookTime(ckRecipe.getPreparationInfo().getCookTime());
		result.setRestTime(ckRecipe.getPreparationInfo().getRestTime());
		result.setDifficulty(ckRecipe.getPreparationInfo().getDifficulty());
		result.setCallories(ckRecipe.getPreparationInfo().getCallories());
		result.setCategories(Arrays.asList(ckRecipe.getRecipeCategories()));
		return result;
	}

}
