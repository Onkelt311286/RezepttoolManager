package de.tkoehler.rezepttool.manager.services.test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import de.tkoehler.rezepttool.manager.services.ImporterServiceException;
import de.tkoehler.rezepttool.manager.services.ImporterServiceImpl;
import de.tkoehler.rezepttool.manager.services.model.ChefkochRecipe;
import de.tkoehler.rezepttool.manager.services.recipeparser.ChefkochRecipeParserImpl;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParserException;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/test.properties")
@RunWith(SpringRunner.class)
public class ImporterServiceIntegrationTest {

	@Autowired
	private ImporterServiceImpl importerService;
	@Autowired
	private ChefkochRecipeParserImpl recipeParser;

	@Test
	public void serviceLoadRecipe_ExistingURL_CorrectIngredCount() throws ImporterServiceException {
		String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		RecipeWebInput recipe = importerService.loadRecipe(url);
		assertThat(recipe.getIngredients().size(), is(7));
	}

	@Test
	public void parserParseRecipe_ExistingURL_CorrectIngredCount() throws RecipeParserException {
		String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		de.tkoehler.rezepttool.manager.services.model.ChefkochRecipe recipe = (ChefkochRecipe) recipeParser.parseRecipe(url);
		assertThat(recipe.getPrintPageData().getIngredients().size(), is(7));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void serviceLoadRecipe_loadIfDBNotEmpty_CorrectIngredNames() throws ImporterServiceException {
		String url1 = "https://www.chefkoch.de/rezepte/2280021363771917/Knoblauch-Champignons.html";
		String url2 = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		RecipeWebInput recipe1 = importerService.loadRecipe(url1);
		importerService.saveRecipe(recipe1);
		RecipeWebInput recipe2 = importerService.loadRecipe(url2);
		assertThat(recipe2.getIngredients(), hasItems(
				hasProperty("name", is("Champignons, kleine frische")), 
				hasProperty("name", is("Knoblauchzehe(n)")),
				hasProperty("name", is("Olivenöl")),
				hasProperty("name", is("Rosmarin")),
				hasProperty("name", is("Balsamico")),
				hasProperty("name", is("Salz")),
				hasProperty("name", is("Blattpetersilie, gehackte"))
				));
	}
}
