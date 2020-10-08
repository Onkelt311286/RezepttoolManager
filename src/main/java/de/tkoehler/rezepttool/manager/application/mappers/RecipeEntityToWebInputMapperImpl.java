package de.tkoehler.rezepttool.manager.application.mappers;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.restcontroller.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;

@Component
public class RecipeEntityToWebInputMapperImpl implements RecipeEntityToWebInputMapper {

    @Override
    public RecipeWebInput process(RecipeEntity recipe) {
        if (recipe == null) return null;
        // @formatter:off
		return RecipeWebInput.builder()
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
				.difficulty(recipe.getDifficulty() != null ? recipe.getDifficulty() : "")
				.ingredients(recipe.getIngredients().parallelStream()
				        .map(ingredient -> IngredientWebInput.builder()
				                .recipeIngredientId(ingredient.getId())
			                    .amount(ingredient.getAmount())
			                    .ingredientId(ingredient.getIngredient().getId())
			                    .name(ingredient.getIngredient().getName())
			                    .originalName(ingredient.getIngredient().getName())
			                    .department(ingredient.getIngredient().getDepartment())
			                    .originalDepartment(ingredient.getIngredient().getDepartment())
			                    .present(ingredient.getIngredient().isPresent())
				                .build())
				        .collect(Collectors.toList()))
				.build();
		// @formatter:on
    }
}
