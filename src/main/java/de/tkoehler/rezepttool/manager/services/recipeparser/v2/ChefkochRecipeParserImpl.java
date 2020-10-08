package de.tkoehler.rezepttool.manager.services.recipeparser.v2;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.tkoehler.rezepttool.manager.services.model.ChefkochRecipeV2;
import de.tkoehler.rezepttool.manager.services.model.Recipe;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParser;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParserException;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ChefkochRecipeParserImpl implements RecipeParser {

    ObjectMapper objectMapper;

    @Override
    public Recipe parseRecipe(String url) throws RecipeParserException {
        Document doc = loadRecipeWebSite(url);
        return parseChefkochRecipe(extractRecipeJSonFromURL(doc));
    }

    public ChefkochRecipeV2 parseChefkochRecipe(String jsonRecipe) {
        try {
            return objectMapper.readValue(jsonRecipe, ChefkochRecipeV2.class);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Document loadRecipeWebSite(String url) throws RecipeParserException {
        checkNullParameter(url);
        Document doc = null;
        try {
            doc = Jsoup.connect(url).timeout(0).get();
        }
        catch (IllegalArgumentException | IOException e) {
            throw new RecipeParserException("Parameter must be a valid URL", e);
        }
        return doc;
    }

    private String extractRecipeJSonFromURL(Document doc) throws RecipeParserException {
        checkNullParameter(doc);
        String data = "";
        Elements script = doc.select("script");
        for (Element element : script) {
            if (element.attr("type").equals("application/ld+json")
                    && element.data().contains("\"@type\": \"Recipe\"")) {
                data = element.data().trim();
            }
        }
        if (data.equals("")) throw new RecipeParserException("Could not find JSON data!");
        return data;
    }

    private void checkNullParameter(Object parameter) throws RecipeParserException {
        if (parameter == null) throw new RecipeParserException("Parameter must not be empty!");
    }
}
