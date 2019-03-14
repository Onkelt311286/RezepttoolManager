package de.tkoehler.rezepttool.manager.application.mappers.test;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.application.mappers.ChefkochRecipeToWebInputMapperImpl;
import de.tkoehler.rezepttool.manager.services.model.AggregateRating;
import de.tkoehler.rezepttool.manager.services.model.Author;
import de.tkoehler.rezepttool.manager.services.model.ChefkochIngredient;
import de.tkoehler.rezepttool.manager.services.model.ChefkochRecipe;
import de.tkoehler.rezepttool.manager.services.model.PreparationInfo;
import de.tkoehler.rezepttool.manager.services.model.PrintPageData;
import de.tkoehler.rezepttool.manager.web.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@RunWith(MockitoJUnitRunner.class)
public class ChefkochRecipeToRecipeMapperTest {

	@InjectMocks
	ChefkochRecipeToWebInputMapperImpl objectUnderTest;
	ChefkochRecipe ckRecipe;

	@Before
	public void setUp() {
		ckRecipe = ChefkochRecipe.builder()
				.url("testURL")
				.context("testContext")
				.type("testType")
				.cookTime("testCookTime")
				.prepTime("testPrepTime")
				.dataPublished("testDatePublished")
				.description("testDescription")
				.image("testImage")
				.recipeIngredients(new String[] { "ingred1", "ingred2", "ingred3" })
				.name("testName")
				.author(Author.builder()
						.type("authorType")
						.name("authorName")
						.build())
				.recipeInstructions("testInstructions")
				.recipeYield("testYield")
				.aggregateRating(AggregateRating.builder()
						.type("aggregateType")
						.ratingValue("testValue")
						.reviewCount("testCount")
						.worstRating(0)
						.bestRating(0)
						.build())
				.recipeCategories(new String[] { "cat1", "cat2", "cat3" })
				.preparationInfo(PreparationInfo.builder()
						.prepTime("testInfoPrepTime")
						.cookTime("testInfoCookTime")
						.restTime("testInfoRestTime")
						.difficulty("testDifficulty")
						.callories("testCallories")
						.build())
				.printPageData(PrintPageData.builder()
						.title("testPrintTitle")
						.additionalInformation("testPrintInfo")
						.yield("testYield")
						.ingredients(new ArrayList<ChefkochIngredient>(Arrays.asList(
								ChefkochIngredient.builder()
										.amount("testAmount1")
										.name("testName1")
										.build(),
								ChefkochIngredient.builder()
										.amount("testAmount2")
										.name("testName2")
										.build())))
						.instructions("testPrintInstructions")
						.build())
				.build();
	}

	@Test
	public void process_NullParameter_returnsNull() {
		RecipeWebInput recipe = objectUnderTest.process(null);
		assertThat(recipe, is(nullValue()));
	}

	@Test
	public void process_TestParamter_NotNull() {
		RecipeWebInput recipe = objectUnderTest.process(ckRecipe);
		assertThat(recipe, is(not(nullValue())));
	}

	@Test
	public void process_TestParamter_TestValues() {
		RecipeWebInput recipe = objectUnderTest.process(ckRecipe);
		assertThat(recipe.getId(), is(""));
		assertThat(recipe.getUrl(), is("testURL"));
		assertThat(recipe.getName(), is("testName"));
		assertThat(recipe.getAdditionalInformation(), is("testPrintInfo"));
		assertThat(recipe.getPortions(), is("testYield"));
		assertThat(recipe.getInstructions(), is("testPrintInstructions"));
		assertThat(recipe.getWorkTime(), is("testInfoPrepTime"));
		assertThat(recipe.getCookTime(), is("testInfoCookTime"));
		assertThat(recipe.getRestTime(), is("testInfoRestTime"));
		assertThat(recipe.getDifficulty(), is("testDifficulty"));
		assertThat(recipe.getCallories(), is("testCallories"));
		assertThat(recipe.getCategories(), hasItems("cat1", "cat2", "cat3"));
		assertThat(recipe.getIngredients(), hasSize(2));
		IngredientWebInput ingred1 = recipe.getIngredients().get(0);
		IngredientWebInput ingred2 = recipe.getIngredients().get(1);
		assertThat(ingred1.getRecipeIngredientId(), is(""));
		assertThat(ingred2.getRecipeIngredientId(), is(""));
		assertThat(ingred1.getIngredientId(), is(""));
		assertThat(ingred2.getIngredientId(), is(""));
		assertThat(ingred1.getAmount(), anyOf(is("testAmount1"), is("testAmount2")));
		assertThat(ingred2.getAmount(), anyOf(is("testAmount1"), is("testAmount2")));
		assertThat(ingred1.getName(), anyOf(is("testName1"), is("testName2")));
		assertThat(ingred2.getName(), anyOf(is("testName1"), is("testName2")));
		assertThat(ingred1.getOriginalName(), anyOf(is("testName1"), is("testName2")));
		assertThat(ingred2.getOriginalName(), anyOf(is("testName1"), is("testName2")));
		assertThat(ingred1.getDepartment(), is(""));
		assertThat(ingred2.getDepartment(), is(""));
		assertThat(ingred1.getOriginalDepartment(), is(""));
		assertThat(ingred2.getOriginalDepartment(), is(""));
		assertThat(ingred1.isUnequalToEntity(), is(false));
		assertThat(ingred2.isUnequalToEntity(), is(false));
	}
}
