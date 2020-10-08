package de.tkoehler.rezepttool.manager.services.recipeparser.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import de.tkoehler.rezepttool.manager.services.model.ChefkochRecipe;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParserException;
import de.tkoehler.rezepttool.manager.services.recipeparser.v1.ChefkochRecipeParserImpl;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/test.properties")
@RunWith(SpringRunner.class)
@DirtiesContext
public class ChefkochRecipeParserIntegrationTest {

	@Autowired
	private ChefkochRecipeParserImpl recipeParser;

	@Test
	public void parserParseRecipe_ExistingURL_CorrectIngredCount() throws RecipeParserException {
		String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		de.tkoehler.rezepttool.manager.services.model.ChefkochRecipe recipe = (ChefkochRecipe) recipeParser.parseRecipe(url);
		assertThat(recipe.getPrintPageData().getIngredients().size(), is(7));
	}
}
