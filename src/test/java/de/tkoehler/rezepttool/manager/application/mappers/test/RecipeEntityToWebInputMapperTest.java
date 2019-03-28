package de.tkoehler.rezepttool.manager.application.mappers.test;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.application.mappers.RecipeEntityToWebInputMapperImpl;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.restcontroller.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;

@RunWith(MockitoJUnitRunner.class)
public class RecipeEntityToWebInputMapperTest {

	@InjectMocks
	RecipeEntityToWebInputMapperImpl objectUnderTest;
	RecipeEntity filledRecipeEntity;

	@Before
	public void setUp() {
		filledRecipeEntity = RecipeEntity.builder()
				.id("testID")
				.url("testUrl")
				.name("testName")
				.additionalInformation("testAdditionalInfo")
				.portions("testPortion")
				.ingredients(Arrays.asList(
						RecipeIngredient.builder()
								.id("testRIngredID1")
								.recipe(filledRecipeEntity)
								.amount("testAmount1")
								.ingredient(Ingredient.builder()
										.id("testIngredID1")
										.name("testIngredName1")
										.alternativeNames(new HashSet<>())
										.department("testDepartment1")
										.build())
								.build(),
						RecipeIngredient.builder()
								.id("testRIngredID2")
								.recipe(filledRecipeEntity)
								.amount("testAmount2")
								.ingredient(Ingredient.builder()
										.id("testIngredID2")
										.name("testIngredName2")
										.alternativeNames(new HashSet<>())
										.department("testDepartment2")
										.build())
								.build()))
				.instructions("testInstructions")
				.workTime("testWorkTime")
				.cookTime("testCookTime")
				.restTime("testRestTime")
				.difficulty("testDifficulty")
				.callories("testCallories")
				.categories(new HashSet<String>(Arrays.asList("cat1", "cat2", "cat3")))
				.build();
	}

	@Test
	public void process_NullParameter_returnsNull() {
		RecipeWebInput recipe = objectUnderTest.process(null);
		assertThat(recipe, is(nullValue()));
	}

	@Test
	public void process_TestParamter_NotNull() {
		RecipeWebInput recipe = objectUnderTest.process(filledRecipeEntity);
		assertThat(recipe, is(not(nullValue())));
	}

	@Test
	public void process_filledTestParamter_filledTestValues() {
		RecipeWebInput recipe = objectUnderTest.process(filledRecipeEntity);
		assertThat(recipe.getId(), is("testID"));
		assertThat(recipe.getUrl(), is("testUrl"));
		assertThat(recipe.getName(), is("testName"));
		assertThat(recipe.getAdditionalInformation(), is("testAdditionalInfo"));
		assertThat(recipe.getPortions(), is("testPortion"));
		assertThat(recipe.getInstructions(), is("testInstructions"));
		assertThat(recipe.getWorkTime(), is("testWorkTime"));
		assertThat(recipe.getCookTime(), is("testCookTime"));
		assertThat(recipe.getRestTime(), is("testRestTime"));
		assertThat(recipe.getDifficulty(), is("testDifficulty"));
		assertThat(recipe.getCallories(), is("testCallories"));
		assertThat(recipe.getCategories(), hasItems("cat1", "cat2", "cat3"));
		assertThat(recipe.getIngredients(), hasSize(2));
		IngredientWebInput recipeIngred1 = recipe.getIngredients().get(0);
		IngredientWebInput recipeIngred2 = recipe.getIngredients().get(1);
		assertThat(recipeIngred1.getRecipeIngredientId(), anyOf(is("testRIngredID1"), is("testRIngredID2")));
		assertThat(recipeIngred2.getRecipeIngredientId(), anyOf(is("testRIngredID1"), is("testRIngredID2")));
		assertThat(recipeIngred1.getIngredientId(), anyOf(is("testIngredID1"), is("testIngredID2")));
		assertThat(recipeIngred2.getIngredientId(), anyOf(is("testIngredID1"), is("testIngredID2")));
		assertThat(recipeIngred1.getAmount(), anyOf(is("testAmount1"), is("testAmount2")));
		assertThat(recipeIngred2.getAmount(), anyOf(is("testAmount1"), is("testAmount2")));
		assertThat(recipeIngred1.getName(), anyOf(is("testIngredName1"), is("testIngredName2")));
		assertThat(recipeIngred2.getName(), anyOf(is("testIngredName1"), is("testIngredName2")));
		assertThat(recipeIngred1.getOriginalName(), anyOf(is("testIngredName1"), is("testIngredName2")));
		assertThat(recipeIngred2.getOriginalName(), anyOf(is("testIngredName1"), is("testIngredName2")));
		assertThat(recipeIngred1.getDepartment(), anyOf(is("testDepartment1"), is("testDepartment2")));
		assertThat(recipeIngred2.getDepartment(), anyOf(is("testDepartment1"), is("testDepartment2")));
		assertThat(recipeIngred1.getOriginalDepartment(), anyOf(is("testDepartment1"), is("testDepartment2")));
		assertThat(recipeIngred2.getOriginalDepartment(), anyOf(is("testDepartment1"), is("testDepartment2")));
		assertThat(recipeIngred1.isUnequalToEntity(), is(false));
		assertThat(recipeIngred2.isUnequalToEntity(), is(false));
	}
	
	@Test
	public void process_NullDifficulty_EmptyDifficulty() {
		filledRecipeEntity.setDifficulty(null);
		RecipeWebInput recipe = objectUnderTest.process(filledRecipeEntity);
		assertThat(recipe.getDifficulty(), is(""));
	}
}
