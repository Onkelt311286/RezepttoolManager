package de.tkoehler.rezepttool.importer.services;

import java.io.IOException;
import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ImporterServiceImpl implements ImporterService {

	/* (non-Javadoc)
	 * @see de.tkoehler.rezepttool.importer.services.ImporterService#importiereRezept(java.lang.String)
	 */
	@Override
	public void importiereRezept(String urlString) {
		System.out.println(urlString);
		try {
			Document doc = Jsoup.connect(urlString).get();
			String data = "";
			Elements script1 = doc.select("script");
			for (Element element : script1) {
				if (element.attr("type").equals("application/ld+json")
						&& element.data().contains("\"@type\": \"Recipe\"")) {
//					System.out.println("Type: " + element.attr("type"));
					System.out.println("data: " + element.data());
					data = element.data().trim();
				}
			}

			JsonReader reader = Json.createReader(new StringReader(data));
			JsonObject jsonResponse = reader.readObject();
			reader.close();

			System.out.println(jsonResponse);
			System.out.println(jsonResponse.getString("@context"));
			System.out.println(jsonResponse.getString("@type"));
			System.out.println(jsonResponse.getString("cookTime"));
			System.out.println(jsonResponse.getString("prepTime"));
			System.out.println(jsonResponse.getString("datePublished"));
			System.out.println(jsonResponse.getString("description"));
			System.out.println(jsonResponse.getString("image"));
			System.out.println(jsonResponse.getJsonArray("recipeIngredient"));
			System.out.println(jsonResponse.getString("name"));
			System.out.println(jsonResponse.getJsonObject("author").getString("@type"));
			System.out.println(jsonResponse.getJsonObject("author").getString("name"));
			System.out.println(jsonResponse.getString("recipeInstructions"));
			System.out.println(jsonResponse.getString("recipeYield"));
			System.out.println(jsonResponse.getJsonObject("aggregateRating").getString("@type"));
			System.out.println(jsonResponse.getJsonObject("aggregateRating").getString("ratingValue"));
			System.out.println(jsonResponse.getJsonObject("aggregateRating").getString("reviewCount"));
			System.out.println(jsonResponse.getJsonObject("aggregateRating").getInt("worstRating"));
			System.out.println(jsonResponse.getJsonObject("aggregateRating").getInt("bestRating"));
			System.out.println(jsonResponse.getJsonArray("recipeCategory"));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
