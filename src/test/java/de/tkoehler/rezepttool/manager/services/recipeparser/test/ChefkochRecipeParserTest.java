package de.tkoehler.rezepttool.manager.services.recipeparser.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.StringReader;
import java.util.Arrays;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.services.model.ChefkochRecipe;
import de.tkoehler.rezepttool.manager.services.model.PreparationInfo;
import de.tkoehler.rezepttool.manager.services.model.PrintPageData;
import de.tkoehler.rezepttool.manager.services.model.Recipe;
import de.tkoehler.rezepttool.manager.services.recipeparser.ChefkochRecipeParserImpl;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParserException;

/**
 * TODO: Hier sind Referenzen auf echte WebSeite, diese Abhängigkeit sollte
 * aufgelöst werden.
 * 
 * @author Arges
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ChefkochRecipeParserTest {

	@InjectMocks
	private ChefkochRecipeParserImpl objectUnderTest;

	@Test(expected = RecipeParserException.class)
	public void loadRecipeWebSite_NullParamter_throwsRecipeParserException() throws Exception {
		objectUnderTest.loadRecipeWebSite(null);
	}

	@Test(expected = RecipeParserException.class)
	public void loadRecipeWebSite_WrongParameter_throwsRecipeParserException() throws Exception {
		objectUnderTest.loadRecipeWebSite("fdsanjkfdsa");
	}

	@Test
	public void loadRecipeWebSite_CorrectData_NotNull() throws Exception {
		String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		Document object = objectUnderTest.loadRecipeWebSite(url);
		assertThat(object, is(not(nullValue())));
	}

	@Test(expected = RecipeParserException.class)
	public void extractRecipeJSonFromURL_NullParamter_throwsRecipeParserException() throws Exception {
		objectUnderTest.extractRecipeJSonFromURL(null);
	}

	@Test(expected = RecipeParserException.class)
	public void extractRecipeJSonFromURL_EmptyWebSite_throwsRecipeParserException() throws Exception {
		objectUnderTest.extractRecipeJSonFromURL(new Document(""));
	}

	@Test(expected = RecipeParserException.class)
	public void extractRecipeJSonFromURL_NoJSonDataInWebSite_throwsRecipeParserException() throws Exception {
		objectUnderTest.extractRecipeJSonFromURL(
				new Document("<!DOCTYPE html><html><head></head><body><script></script></body></html>"));
	}

	@Test
	public void extractRecipeJSonFromURL_CorrectData_success() throws Exception {
		String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		String data = objectUnderTest.extractRecipeJSonFromURL(Jsoup.connect(url).get());
		assertThat(data, containsString("Antipasti - marinierte Champignons"));
	}

	@Test(expected = RecipeParserException.class)
	public void convertJSonStringToJSonObject_NullParamter_throwsRecipeParserException() throws Exception {
		objectUnderTest.convertJSonStringToJSonObject(null);
	}

	@Test(expected = RecipeParserException.class)
	public void convertJSonStringToJSonObject_EmptyParameter_throwsRecipeParserException() throws Exception {
		objectUnderTest.convertJSonStringToJSonObject("");
	}

	@Test
	public void convertJSonStringToJSonObject_CorrectParameter_notNull() throws Exception {
		JsonObject object = objectUnderTest.convertJSonStringToJSonObject("{ \"@context\" : \"TestContext\"}");
		assertThat(object, is(not(nullValue())));
	}

	@Test(expected = RecipeParserException.class)
	public void createChefkochRecipe_NullParameter_throwsRecipeParserException() throws Exception {
		objectUnderTest.createChefkochRecipe(null);
	}

	@Test(expected = RecipeParserException.class)
	public void createChefkochRecipe_WrongParameter_throwsRecipeParserException() throws Exception {
		JsonReader reader = Json.createReader(new StringReader("{}"));
		JsonObject jsonResponse = reader.readObject();
		objectUnderTest.createChefkochRecipe(jsonResponse);
	}

	@Test
	public void createChefkochRecipe_correctParameter_CorrectResultObject() throws Exception {
		JsonReader reader = Json.createReader(new StringReader(
				"{\"@context\": \"context\", \"@type\": \"type\", \"cookTime\": \"cooktime\", \"prepTime\": \"preptime\", \"datePublished\": \"published\", \"description\": \"description\", \"image\": \"image\", \"recipeIngredient\": [ \"ingred1\", \"ingred2\", \"ingred3\"], \"name\": \"name\", \"author\": {	\"@type\": \"authortype\",	\"name\": \"authorname\" }, \"recipeInstructions\": \"instructions\", \"recipeYield\": \"yield\", \"aggregateRating\": {	\"@type\": \"ratingtype\",	\"ratingValue\": \"value\",	\"reviewCount\": \"count\",	\"worstRating\": 0,	\"bestRating\": 0 }, \"recipeCategory\": [\"cat1\", \"cat2\", \"cat3\"]}"));
		JsonObject jsonResponse = reader.readObject();
		ChefkochRecipe recipe = objectUnderTest.createChefkochRecipe(jsonResponse);
		assertThat(recipe.getUrl(), is(nullValue()));
		assertThat(recipe.getContext(), is("context"));
		assertThat(recipe.getType(), is("type"));
		assertThat(recipe.getCookTime(), is("cooktime"));
		assertThat(recipe.getPrepTime(), is("preptime"));
		assertThat(recipe.getDataPublished(), is("published"));
		assertThat(recipe.getDescription(), is("description"));
		assertThat(recipe.getImage(), is("image"));
		assertThat(Arrays.asList(recipe.getRecipeIngredients()), hasItems("\"ingred1\"", "\"ingred2\"", "\"ingred3\""));
		assertThat(recipe.getName(), is("name"));
		assertThat(recipe.getAuthor().getType(), is("authortype"));
		assertThat(recipe.getAuthor().getName(), is("authorname"));
		assertThat(recipe.getRecipeInstructions(), is("instructions"));
		assertThat(recipe.getRecipeYield(), is("yield"));
		assertThat(recipe.getAggregateRating().getType(), is("ratingtype"));
		assertThat(recipe.getAggregateRating().getRatingValue(), is("value"));
		assertThat(recipe.getAggregateRating().getReviewCount(), is("count"));
		assertThat(recipe.getAggregateRating().getWorstRating(), is(0));
		assertThat(recipe.getAggregateRating().getBestRating(), is(0));
		assertThat(recipe.getAggregateRating().getBestRating(), is(0));
		assertThat(Arrays.asList(recipe.getRecipeCategories()), hasItems("\"cat1\"", "\"cat2\"", "\"cat3\""));
	}

	@Test(expected = RecipeParserException.class)
	public void parseRecipe_NullParameter_throwsRecipeParserException() throws Exception {
		objectUnderTest.parseRecipe(null);
	}

	@Test(expected = RecipeParserException.class)
	public void parseRecipe_EmptyParameter_throwsRecipeParserException() throws Exception {
		objectUnderTest.parseRecipe("");
	}

	@Test(expected = RecipeParserException.class)
	public void parseRecipe_WrongParameter_throwsRecipeParserException() throws Exception {
		objectUnderTest.parseRecipe("abcd");
	}

	@Test
	public void parseRecipe_CorrectParameter_NotNull() throws Exception {
		String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		Recipe object = objectUnderTest.parseRecipe(url);
		assertThat(object, is(not(nullValue())));
	}

	@Test(expected = RecipeParserException.class)
	public void extractPreparationInfoFromURL_NullParameter_throwsRecipeParserException() throws Exception {
		objectUnderTest.extractPreparationInfoFromURL(null);
	}

	@Test(expected = RecipeParserException.class)
	public void extractPreparationInfoFromURL_WrongWebSite_throwsRecipeParserException() throws Exception {
		String url = "https://www.chefkoch.de/rezepte/drucken/556631153485020/2309481a/4/Antipasti-marinierte-Champignons.html";
		Document object = objectUnderTest.loadRecipeWebSite(url);
		objectUnderTest.extractPreparationInfoFromURL(object);
	}

	@Test
	public void extractPreparationInfoFromURL_CorrectParameter_NotNull() throws Exception {
		String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		Document object = objectUnderTest.loadRecipeWebSite(url);
		PreparationInfo info = objectUnderTest.extractPreparationInfoFromURL(object);
		assertThat(info, is(not(nullValue())));
	}

	@Test(expected = RecipeParserException.class)
	public void extractAdditionalInformationFromURL_NullParameter_throwsRecipeParserException() throws Exception {
		objectUnderTest.extractAdditionalInformationFromURL(null);
	}

	@Test
	public void extractAdditionalInformationFromURL_WrongWebSite_NullResult() throws Exception {
		String url = "https://stackoverflow.com/questions/41953388/java-split-and-trim-in-one-shot";
		Document object = objectUnderTest.loadRecipeWebSite(url);
		String result = objectUnderTest.extractAdditionalInformationFromURL(object);
		assertThat(result, is(nullValue()));
	}

	@Test
	public void extractAdditionalInformationFromURL_CorrectParameter_NotNull() throws Exception {
		String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		Document object = objectUnderTest.loadRecipeWebSite(url);
		String info = objectUnderTest.extractAdditionalInformationFromURL(object);
		assertThat(info, is(not(nullValue())));
	}

	@Test(expected = RecipeParserException.class)
	public void createPrintPageData_NullParameter_throwsRecipeParserException() throws Exception {
		objectUnderTest.createPrintPageData(null);
	}

	@Test(expected = RecipeParserException.class)
	public void createPrintPageData_WrongWebSite_throwsRecipeParserException() throws Exception {
		String url = "https://stackoverflow.com/questions/41953388/java-split-and-trim-in-one-shot";
		Document object = objectUnderTest.loadRecipeWebSite(url);
		objectUnderTest.createPrintPageData(object);
	}

	@Test
	public void createPrintPageData_CorrectParameter_NotNull() throws Exception {
		String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		Document object = objectUnderTest.loadRecipeWebSite(url);
		PrintPageData data = objectUnderTest.createPrintPageData(object);
		assertThat(data, is(not(nullValue())));
	}

	@Test(expected = RecipeParserException.class)
	public void extractPrintPageData_NullParameter_throwsRecipeParserException() throws Exception {
		objectUnderTest.extractPrintPageData(null);
	}

	@Test(expected = RecipeParserException.class)
	public void extractPrintPageData_WrongWebSite_throwsRecipeParserException() throws Exception {
		String url = "https://stackoverflow.com/questions/41953388/java-split-and-trim-in-one-shot";
		Document object = objectUnderTest.loadRecipeWebSite(url);
		objectUnderTest.extractPrintPageData(object);
	}

	@Test
	public void extractPrintPageData_CorrectParameter_NotNull() throws Exception {
		String url = "https://www.chefkoch.de/rezepte/drucken/556631153485020/2309481a/4/Antipasti-marinierte-Champignons.html";
		Document object = objectUnderTest.loadRecipeWebSite(url);
		PrintPageData data = objectUnderTest.extractPrintPageData(object);
		assertThat(data, is(not(nullValue())));
	}
}
