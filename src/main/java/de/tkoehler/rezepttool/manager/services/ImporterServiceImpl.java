package de.tkoehler.rezepttool.manager.services;

import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.tkoehler.rezepttool.manager.services.model.ChefkochRezept;

public class ImporterServiceImpl implements ImporterService {

	@Override
	public void importRecipe(String urlString) throws ImporterServiceException {
		if(urlString == null) throw new ImporterServiceException("Paramter must not be empty!");
		Document doc = null;
		try {
			doc = Jsoup.connect(urlString).get();
		} catch (IllegalArgumentException | IOException e) {
			throw new ImporterServiceException("Paramter must be a valid URL", e);
		}
		
		String data = extractRecipeJSonFromURL(doc);

//		JsonReader reader = Json.createReader(new StringReader(data));
//		JsonObject jsonResponse = reader.readObject();
//		reader.close();
//		
//		ChefkochRezept rezept = ChefkochRezept.builder()
//				.context(jsonResponse.getString("@context"))
//				.type(jsonResponse.getString("@type"))
//				.cookTime(jsonResponse.getString("cookTime"))
//				.prepTime(jsonResponse.getString("prepTime"))
//				.dataPublished(jsonResponse.getString("datePublished"))
//				.description(jsonResponse.getString("description"))
//				.image(jsonResponse.getString("image"))
//				.recipeIngredient(jsonResponse.getJsonArray("recipeIngredient").toArray(new String[0]))
//				.build();
//
//		System.out.println(jsonResponse);
//		System.out.println(jsonResponse.getString("@context"));
//		System.out.println(jsonResponse.getString("@type"));
//		System.out.println(jsonResponse.getString("cookTime"));
//		System.out.println(jsonResponse.getString("prepTime"));
//		System.out.println(jsonResponse.getString("datePublished"));
//		System.out.println(jsonResponse.getString("description"));
//		System.out.println(jsonResponse.getString("image"));
//		System.out.println(jsonResponse.getJsonArray("recipeIngredient"));
//		System.out.println(jsonResponse.getString("name"));
//		System.out.println(jsonResponse.getJsonObject("author").getString("@type"));
//		System.out.println(jsonResponse.getJsonObject("author").getString("name"));
//		System.out.println(jsonResponse.getString("recipeInstructions"));
//		System.out.println(jsonResponse.getString("recipeYield"));
//		System.out.println(jsonResponse.getJsonObject("aggregateRating").getString("@type"));
//		System.out.println(jsonResponse.getJsonObject("aggregateRating").getString("ratingValue"));
//		System.out.println(jsonResponse.getJsonObject("aggregateRating").getString("reviewCount"));
//		System.out.println(jsonResponse.getJsonObject("aggregateRating").getInt("worstRating"));
//		System.out.println(jsonResponse.getJsonObject("aggregateRating").getInt("bestRating"));
//		System.out.println(jsonResponse.getJsonArray("recipeCategory"));
	}

	public String extractRecipeJSonFromURL(Document doc) throws ImporterServiceException {
		if(doc == null) throw new ImporterServiceException("Paramter must not be empty!");
		String data = "";
		Elements script = doc.select("script");
		for (Element element : script) {
			if (element.attr("type").equals("application/ld+json")
					&& element.data().contains("\"@type\": \"Recipe\"")) {
				data = element.data().trim();
			}
		}
		if(data.equals("")) throw new ImporterServiceException("Could not find JSON data!");
		return data;
	}
}
