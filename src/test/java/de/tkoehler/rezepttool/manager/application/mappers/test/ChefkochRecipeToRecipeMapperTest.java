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

import de.tkoehler.rezepttool.manager.application.mappers.ChefkochRecipeToRecipeMapper;
import de.tkoehler.rezepttool.manager.repositories.model.Difficulty;
import de.tkoehler.rezepttool.manager.repositories.model.Recipe;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.services.model.AggregateRating;
import de.tkoehler.rezepttool.manager.services.model.Author;
import de.tkoehler.rezepttool.manager.services.model.ChefkochIngredient;
import de.tkoehler.rezepttool.manager.services.model.ChefkochRecipe;
import de.tkoehler.rezepttool.manager.services.model.PreparationInfo;
import de.tkoehler.rezepttool.manager.services.model.PrintPageData;

@RunWith(MockitoJUnitRunner.class)
public class ChefkochRecipeToRecipeMapperTest {

	@InjectMocks
	ChefkochRecipeToRecipeMapper objectUnderTest;
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
		Recipe recipe = objectUnderTest.process(null);
		assertThat(recipe, is(nullValue()));
	}

	@Test
	public void process_TestParamter_NotNull() {
		Recipe recipe = objectUnderTest.process(ckRecipe);
		assertThat(recipe, is(not(nullValue())));
	}

	@Test
	public void process_TestParamter_TestValues() {
		Recipe recipe = objectUnderTest.process(ckRecipe);
		assertThat(recipe.getId(), is(not(nullValue())));
		assertThat(recipe.getId(), is(not("")));
		assertThat(recipe.getUrl(), is("testURL"));
		assertThat(recipe.getName(), is("testName"));
		assertThat(recipe.getAdditionalInformation(), is("testPrintInfo"));
		assertThat(recipe.getPortions(), is("testYield"));
		assertThat(recipe.getInstructions(), is("testPrintInstructions"));
		assertThat(recipe.getWorkTime(), is("testInfoPrepTime"));
		assertThat(recipe.getCookTime(), is("testInfoCookTime"));
		assertThat(recipe.getRestTime(), is("testInfoRestTime"));
		assertThat(recipe.getDifficulty(), is(Difficulty.SIMPEL));
		assertThat(recipe.getCallories(), is("testCallories"));
		assertThat(recipe.getCategories(), hasItems("cat1", "cat2", "cat3"));
		assertThat(recipe.getIngredients(), hasSize(2));
		RecipeIngredient ingred1 = recipe.getIngredients().get(0);
		RecipeIngredient ingred2 = recipe.getIngredients().get(1);
		assertThat(ingred1.getAmount(), anyOf(is("testAmount1"), is("testAmount2")));
		assertThat(ingred2.getAmount(), anyOf(is("testAmount1"), is("testAmount2")));
		assertThat(ingred1.getRecipe(), is(recipe));
		assertThat(ingred2.getRecipe(), is(recipe));
		assertThat(ingred1.getIngredient().getAlternativeNames(), hasSize(1));
		assertThat(ingred2.getIngredient().getAlternativeNames(), hasSize(1));
		assertThat(ingred1.getIngredient().getAlternativeNames().get(0), anyOf(is("testName1"), is("testName2")));
		assertThat(ingred2.getIngredient().getAlternativeNames().get(0), anyOf(is("testName1"), is("testName2")));
		assertThat(ingred1.getIngredient().getRecipeIngredients().get(0), is(ingred1));
		assertThat(ingred2.getIngredient().getRecipeIngredients().get(0), is(ingred2));
	}
}
