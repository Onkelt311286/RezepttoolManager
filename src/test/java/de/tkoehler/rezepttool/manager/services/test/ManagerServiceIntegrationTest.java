package de.tkoehler.rezepttool.manager.services.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.services.ManagerServiceException;
import de.tkoehler.rezepttool.manager.services.ManagerServiceImpl;
import de.tkoehler.rezepttool.manager.web.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/test.properties")
@RunWith(SpringRunner.class)
@DirtiesContext
public class ManagerServiceIntegrationTest {

	@Autowired
	private ManagerServiceImpl managerService;
	@Autowired
	private IngredientRepository ingredientRepository;

	@Test
	public void saveRecipe_duplicateIngredient_savesNoDuplicate() throws ManagerServiceException {
		RecipeWebInput recipe1 = RecipeWebInput.builder()
				.id("testId1")
				.name("Recipe1")
				.url("Recipe1Url")
				.instructions("Instructions1")
				.difficulty("simpel")
				.ingredients(Arrays.asList(
						IngredientWebInput.builder()
								.ingredientId("testIngredId1")
								.recipeIngredientId("testRecipeIngredId1")
								.name("Ingredient1")
								.department("Department1")
								.originalName("Ingredient1")
								.build(),
						IngredientWebInput.builder()
								.ingredientId("testIngredId2")
								.recipeIngredientId("testRecipeIngredId2")
								.name("Ingredient2")
								.department("Department2")
								.originalName("Ingredient")
								.build()))

				.build();
		RecipeWebInput recipe2 = RecipeWebInput.builder()
				.id("testId2")
				.name("Recipe2")
				.url("Recipe2Url")
				.instructions("Instructions2")
				.difficulty("simpel")
				.ingredients(Arrays.asList(
						IngredientWebInput.builder()
								.ingredientId("testIngredId3")
								.recipeIngredientId("testRecipeIngredId3")
								.name("Ingredient3")
								.department("Department3")
								.originalName("Ingredient3")
								.build(),
						IngredientWebInput.builder()
								.ingredientId("testIngredId2")
								.recipeIngredientId("testRecipeIngredId2")
								.name("Ingredient2")
								.department("Department2")
								.originalName("Ingredient")
								.build()))
				.build();
		managerService.saveRecipe(recipe1);
		managerService.saveRecipe(recipe2);

		assertThat(ingredientRepository.count(), is(3L));
	}
}
