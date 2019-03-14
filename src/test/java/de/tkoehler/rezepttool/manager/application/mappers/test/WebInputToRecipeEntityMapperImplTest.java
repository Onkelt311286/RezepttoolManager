package de.tkoehler.rezepttool.manager.application.mappers.test;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.application.mappers.WebInputToRecipeEntityMapperImpl;
import de.tkoehler.rezepttool.manager.repositories.model.Difficulty;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.web.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@RunWith(MockitoJUnitRunner.class)
public class WebInputToRecipeEntityMapperImplTest {

	@InjectMocks
	WebInputToRecipeEntityMapperImpl objectUnderTest;
	RecipeWebInput filledRecipeInput;
	RecipeWebInput emptyRecipeInput;

	@Before
	public void setUp() {
		filledRecipeInput = RecipeWebInput.builder()
				.id("testId")
				.url("testURL")
				.name("testName")
				.additionalInformation("testInfo")
				.portions("portions")
				.ingredients(Arrays.asList(
						IngredientWebInput.builder()
								.ingredientId("testIngredId1")
								.recipeIngredientId("testRecipeIngredId1")
								.amount("testAmount1")
								.name("testIngredName1")
								.originalName("testOrigName1")
								.department("testDepartment1")
								.build(),
						IngredientWebInput.builder()
								.ingredientId("testIngredId2")
								.recipeIngredientId("testRecipeIngredId2")
								.amount("testAmount2")
								.name("testIngredName2")
								.originalName("testOrigName2")
								.department("testDepartment2")
								.build()))
				.instructions("testInsturctions")
				.workTime("testWTime")
				.cookTime("testCookTime")
				.restTime("testRestTime")
				.difficulty("simpel")
				.callories("testCallories")
				.categories(Arrays.asList("cat1", "cat2", "cat3"))
				.build();

		emptyRecipeInput = RecipeWebInput.builder()
				.id("")
				.url("")
				.name("")
				.additionalInformation("")
				.portions("")
				.ingredients(Arrays.asList(
						IngredientWebInput.builder()
								.ingredientId("")
								.recipeIngredientId("")
								.amount("")
								.name("")
								.originalName("")
								.department("")
								.build(),
						IngredientWebInput.builder()
								.ingredientId("")
								.recipeIngredientId("")
								.amount("")
								.name("")
								.originalName("")
								.department("")
								.build()))
				.instructions("")
				.workTime("")
				.cookTime("")
				.restTime("")
				.difficulty("")
				.callories("")
				.categories(new ArrayList<>())
				.build();
	}

	@Test
	public void process_NullParameter_returnsNull() {
		RecipeEntity recipe = objectUnderTest.process(null);
		assertThat(recipe, is(nullValue()));
	}

	@Test
	public void process_TestParamter_NotNull() {
		RecipeEntity recipe = objectUnderTest.process(filledRecipeInput);
		assertThat(recipe, is(not(nullValue())));
	}

	@Test
	public void process_emptyTestParamter_generatedIDs() {
		RecipeEntity recipe = objectUnderTest.process(emptyRecipeInput);
		RecipeIngredient recipeIngred1 = recipe.getIngredients().get(0);
		RecipeIngredient recipeIngred2 = recipe.getIngredients().get(1);
		Ingredient ingred1 = recipeIngred1.getIngredient();
		Ingredient ingred2 = recipeIngred2.getIngredient();

		assertThat(recipe.getId(), not(is(nullValue())));
		assertThat(recipe.getId(), not(is("")));
		assertThat(recipeIngred1.getId(), not(is(nullValue())));
		assertThat(recipeIngred1.getId(), not(is("")));
		assertThat(ingred1.getId(), not(is(nullValue())));
		assertThat(ingred1.getId(), not(is("")));
		assertThat(recipeIngred2.getId(), not(is(nullValue())));
		assertThat(recipeIngred2.getId(), not(is("")));
		assertThat(ingred2.getId(), not(is(nullValue())));
		assertThat(ingred2.getId(), not(is("")));

		// Wold Thro IllegalArgumentException, if UUID Generation wouldn't have worked.
		UUID.fromString(recipe.getId());
		UUID.fromString(recipeIngred1.getId());
		UUID.fromString(recipeIngred2.getId());
		UUID.fromString(ingred1.getId());
		UUID.fromString(ingred2.getId());
	}

	@Test
	public void process_filledTestParamter_filledTestValues() {
		RecipeEntity recipe = objectUnderTest.process(filledRecipeInput);
		assertThat(recipe.getId(), is("testId"));
		assertThat(recipe.getUrl(), is("testURL"));
		assertThat(recipe.getName(), is("testName"));
		assertThat(recipe.getAdditionalInformation(), is("testInfo"));
		assertThat(recipe.getPortions(), is("portions"));
		assertThat(recipe.getInstructions(), is("testInsturctions"));
		assertThat(recipe.getWorkTime(), is("testWTime"));
		assertThat(recipe.getCookTime(), is("testCookTime"));
		assertThat(recipe.getRestTime(), is("testRestTime"));
		assertThat(recipe.getDifficulty(), is(Difficulty.SIMPEL));
		assertThat(recipe.getCallories(), is("testCallories"));
		assertThat(recipe.getCategories(), hasItems("cat1", "cat2", "cat3"));
		assertThat(recipe.getIngredients(), hasSize(2));
		RecipeIngredient recipeIngred1 = recipe.getIngredients().get(0);
		RecipeIngredient recipeIngred2 = recipe.getIngredients().get(1);
		assertThat(recipeIngred1.getId(), anyOf(is("testRecipeIngredId1"), is("testRecipeIngredId2")));
		assertThat(recipeIngred2.getId(), anyOf(is("testRecipeIngredId1"), is("testRecipeIngredId2")));
		assertThat(recipeIngred1.getRecipe(), is(recipe));
		assertThat(recipeIngred2.getRecipe(), is(recipe));
		assertThat(recipeIngred1.getAmount(), anyOf(is("testAmount1"), is("testAmount2")));
		assertThat(recipeIngred2.getAmount(), anyOf(is("testAmount1"), is("testAmount2")));
		assertThat(recipeIngred1.getIngredient(), is(not(nullValue())));
		assertThat(recipeIngred2.getIngredient(), is(not(nullValue())));
		Ingredient ingred1 = recipeIngred1.getIngredient();
		Ingredient ingred2 = recipeIngred2.getIngredient();
		assertThat(ingred1.getId(), anyOf(is("testIngredId1"), is("testIngredId2")));
		assertThat(ingred2.getId(), anyOf(is("testIngredId1"), is("testIngredId2")));
		assertThat(ingred1.getName(), anyOf(is("testIngredName1"), is("testIngredName2")));
		assertThat(ingred2.getName(), anyOf(is("testIngredName1"), is("testIngredName2")));
		assertThat(ingred1.getDepartment(), anyOf(is("testDepartment1"), is("testDepartment2")));
		assertThat(ingred2.getDepartment(), anyOf(is("testDepartment1"), is("testDepartment2")));
		assertThat(ingred1.getAlternativeNames(), anyOf(hasItems("testOrigName1"), hasItems("testOrigName2")));
		assertThat(ingred2.getAlternativeNames(), anyOf(hasItems("testOrigName1"), hasItems("testOrigName2")));
	}
}
