package de.tkoehler.rezepttool.manager.services.recipeparser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.services.model.AggregateRating;
import de.tkoehler.rezepttool.manager.services.model.Author;
import de.tkoehler.rezepttool.manager.services.model.ChefkochIngredient;
import de.tkoehler.rezepttool.manager.services.model.ChefkochRecipe;
import de.tkoehler.rezepttool.manager.services.model.PreparationInfo;
import de.tkoehler.rezepttool.manager.services.model.PrintPageData;
import de.tkoehler.rezepttool.manager.services.model.Recipe;

@Component
public class ChefkochRecipeParserImpl implements RecipeParser {

	@Override
	public Recipe parseRecipe(String url) throws RecipeParserException {
		Document doc = loadRecipeWebSite(url);
		String data = extractRecipeJSonFromURL(doc);
		JsonObject jsonResponse = convertJSonStringToJSonObject(data);
		ChefkochRecipe recipe = createChefkochRecipe(jsonResponse);
		recipe.setUrl(url);
		recipe.setPreparationInfo(extractPreparationInfoFromURL(doc));
		recipe.setAdditionalInformation(extractAdditionalInformationFromURL(doc));
		recipe.setPrintPageData(createPrintPageData(doc));
		return recipe;
	}

	public PrintPageData createPrintPageData(Document doc) throws RecipeParserException {
		checkNullParameter(doc);
		Element recipeButtons = doc.getElementById("recipe-buttons");
		if (recipeButtons == null) throw new RecipeParserException("Could not find recipe print button in website");
		String printUrl = "https://www.chefkoch.de";
		for (Element element : recipeButtons.getElementsByAttribute("href")) {
			printUrl += element.attr("href");
			break;
		}
		Document printDocument = loadRecipeWebSite(printUrl);
		PrintPageData result = extractPrintPageData(printDocument);
		return result;
	}

	public PrintPageData extractPrintPageData(Document doc) throws RecipeParserException {
		checkNullParameter(doc);
		Elements elements = doc.getElementsByAttributeValue("class", "article clearfix");
		if(elements.size() <= 0) throw new RecipeParserException("Could not find recipe print data in website");
		Element section = elements.get(0);
		String title = section.getElementsByAttributeValue("class", " neg-m-t ").size() > 0 ? section.getElementsByAttributeValue("class", " neg-m-t ").get(0).text().trim() : "";
		String additionalInfo = section.select("strong").size() > 0 ? section.select("strong").get(0).text().trim() : "";
		String yield = section.select("h3").size() > 0 ? section.select("h3").get(0).text().trim() : "";
		elements = doc.getElementsByAttributeValue("class", "content-left");
		String instructions = elements.size() > 0 ? elements.get(0).text().split("Arbeitszeit")[0].trim() : "";
		List<ChefkochIngredient> ingredients = new ArrayList<>();
		Elements ingredientElements = section.select("tbody").select("tr");
		for (Element element : ingredientElements) {
			String amount = extractIngredientValue(element, "class", "amount");
			String name = extractIngredientValue(element, "valign", "top");
			if(amount != null && name !=null) {
				ChefkochIngredient ingred = ChefkochIngredient.builder()
						.amount(amount)
						.name(name)
						.build();
				ingredients.add(ingred);
			}
		}
		return PrintPageData.builder()
				.title(title)
				.additionalInformation(additionalInfo)
				.yield(yield)
				.instructions(instructions)
				.ingredients(ingredients)
				.build();
	}

	public String extractIngredientValue(Element element, String key, String value) {
		Elements elements = element.getElementsByAttributeValue(key, value);
		if (elements.size() > 0)
			return elements.get(0).text().trim();
		return null;
	}

	public ChefkochRecipe createChefkochRecipe(JsonObject jsonObject) throws RecipeParserException {
		checkNullParameter(jsonObject);
		try {
			return ChefkochRecipe.builder()
					.context(jsonObject.getString("@context"))
					.type(jsonObject.getString("@type"))
					.cookTime(jsonObject.getString("cookTime"))
					.prepTime(jsonObject.getString("prepTime"))
					.dataPublished(jsonObject.getString("datePublished"))
					.description(jsonObject.getString("description"))
					.image(jsonObject.getString("image"))
					.recipeIngredients(Arrays.stream(jsonObject.getJsonArray("recipeIngredient").toArray()).map(Object::toString).toArray(String[]::new))
					.name(jsonObject.getString("name"))
					.author(Author.builder()
							.type(jsonObject.getJsonObject("author").getString("@type"))
							.name(jsonObject.getJsonObject("author").getString("name"))
							.build())
					.recipeInstructions(jsonObject.getString("recipeInstructions"))
					.recipeYield(jsonObject.getString("recipeYield"))
					.aggregateRating(AggregateRating.builder()
							.type(jsonObject.getJsonObject("aggregateRating").getString("@type"))
							.ratingValue(jsonObject.getJsonObject("aggregateRating").getString("ratingValue"))
							.reviewCount(jsonObject.getJsonObject("aggregateRating").getString("reviewCount"))
							.worstRating(jsonObject.getJsonObject("aggregateRating").getInt("worstRating"))
							.bestRating(jsonObject.getJsonObject("aggregateRating").getInt("bestRating"))
							.build())
					.recipeCategories(Arrays.stream(jsonObject.getJsonArray("recipeCategory").toArray()).map(Object::toString).toArray(String[]::new))
					.build();
		}
		catch (NullPointerException e) {
			throw new RecipeParserException("JSonObject could not be parsed to Recipe!", e);
		}
	}

	public JsonObject convertJSonStringToJSonObject(String data) throws RecipeParserException {
		checkNullParameter(data);
		JsonReader reader = Json.createReader(new StringReader(data));
		JsonObject jsonResponse = null;
		try {
			jsonResponse = reader.readObject();
		}
		catch (JsonException e) {
			throw new RecipeParserException("Parameter must be a valid JSon String", e);
		}
		reader.close();
		return jsonResponse;
	}

	public Document loadRecipeWebSite(String url) throws RecipeParserException {
		checkNullParameter(url);
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		}
		catch (IllegalArgumentException | IOException e) {
			throw new RecipeParserException("Parameter must be a valid URL", e);
		}
		return doc;
	}

	public String extractRecipeJSonFromURL(Document doc) throws RecipeParserException {
		checkNullParameter(doc);
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

	public PreparationInfo extractPreparationInfoFromURL(Document doc) throws RecipeParserException {
		checkNullParameter(doc);
		PreparationInfo preparationInfo = new PreparationInfo();
		Element paragraph = doc.getElementById("preparation-info");
		if (paragraph == null)
			throw new RecipeParserException("Could not find preparation-info id");
		String[] infos = paragraph.text().replace("Koch-/Backzeit:", "Koch-Backzeit:").replace("Ruhezeit", "/ Ruhezeit").split("/");
		for (String info : infos) {
			info = info.trim();
			if (info.startsWith("Arbeitszeit:"))
				preparationInfo.setPrepTime(info.replace("Arbeitszeit", "").trim());
			if (info.startsWith("Koch-Backzeit:"))
				preparationInfo.setCookTime(info.replace("Koch-Backzeit:", "").trim());
			if (info.startsWith("Ruhezeit:"))
				preparationInfo.setRestTime(info.replace("Ruhezeit:", "").trim());
			if (info.startsWith("Schwierigkeitsgrad:"))
				preparationInfo.setDifficulty(info.replace("Schwierigkeitsgrad:", "").trim());
			if (info.startsWith("Kalorien p. P.:"))
				preparationInfo.setCallories(info.replace("Kalorien p. P.:", "").trim());
		}
		return preparationInfo;
	}

	public String extractAdditionalInformationFromURL(Document doc) throws RecipeParserException {
		checkNullParameter(doc);
		Elements div = doc.getElementsByClass("summary");
		for (Element element : div) {
			return element.text();
		}
		return null;
	}

	private void checkNullParameter(Object parameter) throws RecipeParserException {
		if (parameter == null)
			throw new RecipeParserException("Parameter must not be empty!");
	}
}
