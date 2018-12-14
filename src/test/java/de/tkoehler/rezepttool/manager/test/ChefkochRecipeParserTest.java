package de.tkoehler.rezepttool.manager.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.services.ChefkochRecipeParserImpl;
import de.tkoehler.rezepttool.manager.services.RecipeParserException;
import de.tkoehler.rezepttool.manager.services.model.ChefkochRecipe;

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
	public void loadRecipeWebSite_CorrectData_success() throws Exception {
		String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		objectUnderTest.loadRecipeWebSite(url);
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
	public void createChefkochRecipe_correctParameter_success() throws Exception {
		JsonReader reader = Json.createReader(new StringReader("{\"@context\": \"context\", \"@type\": \"type\", \"cookTime\": \"cooktime\", \"prepTime\": \"preptime\", \"datePublished\": \"published\", \"description\": \"description\", \"image\": \"image\", \"recipeIngredient\": [ \"ingred1\", \"ingred2\", \"ingred3 \"], \"name\": \"name\", \"author\": {	\"@type\": \"authortype\",	\"name\": \"authorname\" }, \"recipeInstructions\": \"instructions\", \"recipeYield\": \"yield\", \"aggregateRating\": {	\"@type\": \"ratingtype\",	\"ratingValue\": \"value\",	\"reviewCount\": \"count\",	\"worstRating\": 0,	\"bestRating\": 0 }, \"recipeCategory\": [\"cat1\", \"cat2\", \"cat3\"]}"));
		JsonObject jsonResponse =  reader.readObject();
		ChefkochRecipe recipe = objectUnderTest.createChefkochRecipe(jsonResponse);
		assertThat(recipe.getContext(), is("context"));
		assertThat(recipe.getType(), is("type"));
		
		assertThat(recipe.getCookTime(), is("cooktime"));
		assertThat(recipe.getPrepTime(), is("preptime"));
		assertThat(recipe.getDataPublished(), is("published"));
		assertThat(recipe.getDescription(), is("description"));
		assertThat(recipe.getImage(), is("image"));
		String[] ingreds = {"ingred1", "ingred2", "ingred3"};
		assertThat(recipe.getRecipeIngredient(), is(ingreds));
//		assertThat(recipe.getType(), is("type"));
//		assertThat(recipe.getType(), is("type"));
//		assertThat(recipe.getType(), is("type"));
//		assertThat(recipe.getType(), is("type"));
//		assertThat(recipe.getType(), is("type"));
//		assertThat(recipe.getType(), is("type"));
//		assertThat(recipe.getType(), is("type"));
//		assertThat(recipe.getType(), is("type"));
	}
}
