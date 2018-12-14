package de.tkoehler.rezepttool.manager.services;

import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.services.model.ChefkochRecipe;
import de.tkoehler.rezepttool.manager.services.model.Recipe;

@Component
public class ChefkochRecipeParserImpl implements RecipeParser {

	@Override
	public Recipe parseRecipe(String url) throws RecipeParserException {
		Document doc = loadRecipeWebSite(url);
		String data = extractRecipeJSonFromURL(doc);
		JsonObject jsonResponse = convertJSonStringToJSonObject(data);
		return createChefkochRecipe(jsonResponse);

		// System.out.println(jsonResponse);
		// System.out.println(jsonResponse.getString("@context"));
		// System.out.println(jsonResponse.getString("@type"));
		// System.out.println(jsonResponse.getString("cookTime"));
		// System.out.println(jsonResponse.getString("prepTime"));
		// System.out.println(jsonResponse.getString("datePublished"));
		// System.out.println(jsonResponse.getString("description"));
		// System.out.println(jsonResponse.getString("image"));
		// System.out.println(jsonResponse.getJsonArray("recipeIngredient"));
		// System.out.println(jsonResponse.getString("name"));
		// System.out.println(jsonResponse.getJsonObject("author").getString("@type"));
		// System.out.println(jsonResponse.getJsonObject("author").getString("name"));
		// System.out.println(jsonResponse.getString("recipeInstructions"));
		// System.out.println(jsonResponse.getString("recipeYield"));
		// System.out.println(jsonResponse.getJsonObject("aggregateRating").getString("@type"));
		// System.out.println(jsonResponse.getJsonObject("aggregateRating").getString("ratingValue"));
		// System.out.println(jsonResponse.getJsonObject("aggregateRating").getString("reviewCount"));
		// System.out.println(jsonResponse.getJsonObject("aggregateRating").getInt("worstRating"));
		// System.out.println(jsonResponse.getJsonObject("aggregateRating").getInt("bestRating"));
		// System.out.println(jsonResponse.getJsonArray("recipeCategory"));
	}

	public ChefkochRecipe createChefkochRecipe(JsonObject jsonObject) throws RecipeParserException {
		if (jsonObject == null)
			throw new RecipeParserException("Parameter must not be empty!");
		ChefkochRecipe result = null;
		try {
			result = ChefkochRecipe.builder()
					.context(jsonObject.getString("@context"))
					.type(jsonObject.getString("@type"))
					.cookTime(jsonObject.getString("cookTime"))
					.prepTime(jsonObject.getString("prepTime"))
					.dataPublished(jsonObject.getString("datePublished"))
					.description(jsonObject.getString("description"))
					.image(jsonObject.getString("image"))
					.build();
		} catch (NullPointerException e) {
			throw new RecipeParserException("JSonObject could not be parsed to Recipe!", e);
		}
		// .recipeIngredient(jsonResponse.getJsonArray("recipeIngredient").toArray(new
		// String[0]))
		// .build();
		return result;
	}

	public JsonObject convertJSonStringToJSonObject(String data) throws RecipeParserException {
		if (data == null)
			throw new RecipeParserException("Parameter must not be empty!");
		JsonReader reader = Json.createReader(new StringReader(data));
		JsonObject jsonResponse = null;
		try {
			jsonResponse = reader.readObject();
		} catch (JsonException e) {
			throw new RecipeParserException("Parameter must be a valid JSon String", e);
		}
		reader.close();
		return jsonResponse;
	}

	public Document loadRecipeWebSite(String url) throws RecipeParserException {
		if (url == null)
			throw new RecipeParserException("Parameter must not be empty!");
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IllegalArgumentException | IOException e) {
			throw new RecipeParserException("Parameter must be a valid URL", e);
		}
		return doc;
	}

	public String extractRecipeJSonFromURL(Document doc) throws RecipeParserException {
		if (doc == null)
			throw new RecipeParserException("Parameter must not be empty!");
		String data = "";
		Elements script = doc.select("script");
		for (Element element : script) {
			if (element.attr("type").equals("application/ld+json")
					&& element.data().contains("\"@type\": \"Recipe\"")) {
				data = element.data().trim();
			}
		}
		if (data.equals(""))
			throw new RecipeParserException("Could not find JSON data!");
		return data;
	}
}
