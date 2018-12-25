package de.tkoehler.rezepttool.manager.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Difficulty;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.Recipe;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;

@RestController
@RequestMapping("/rezept")
public class RecipeController {

	private final RecipeRepository recipeRepository;

	public RecipeController(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	@RequestMapping(path = "/savedummy", method = RequestMethod.GET)
	public ResponseEntity<Void> saveDummy() {
		long count = recipeRepository.count();

		Recipe recipe = Recipe.builder()
				.id(UUID.randomUUID().toString())
				.url("dummyURL" + count)
				.name("dummyName" + count)
				.additionalInformationen("dummyInfo")
				.portions("100")
				.instructions("dummyInstructions")
				.workTime("10min")
				.cookTime("20min")
				.restTime("30min")
				.difficulty(Difficulty.PFIFFIG)
				.callories("10000")
				.categories(new ArrayList<String>(Arrays.asList("cat1" + count, "cat2" + count)))
				.build();

		Ingredient ingred1 = Ingredient.builder()
				.id(UUID.randomUUID().toString())
				.name("dummyIngredName1" + count)
				.alternativeNames(Arrays.asList("altName1" + count))
				.build();

		RecipeIngredient recipeIngred1 = RecipeIngredient.builder()
				.id(UUID.randomUUID().toString())
				.recipe(recipe)
				.amount("100 g")
				.ingredient(ingred1)
				.build();
		ingred1.addIngredient(recipeIngred1);

		Ingredient ingred2 = Ingredient.builder()
				.id(UUID.randomUUID().toString())
				.name("dummyIngredName2" + count)
				.alternativeNames(Arrays.asList("altName2" + count))
				.build();

		RecipeIngredient recipeIngred2 = RecipeIngredient.builder()
				.id(UUID.randomUUID().toString())
				.recipe(recipe)
				.amount("100 g")
				.ingredient(ingred2)
				.build();
		ingred2.addIngredient(recipeIngred2);

		recipe.addRecipeIngredient(recipeIngred1);
		recipe.addRecipeIngredient(recipeIngred2);

		recipeRepository.save(recipe);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
