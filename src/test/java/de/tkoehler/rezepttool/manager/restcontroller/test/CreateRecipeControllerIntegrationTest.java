package de.tkoehler.rezepttool.manager.restcontroller.test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import de.tkoehler.rezepttool.manager.restcontroller.CreateRecipeController;
import de.tkoehler.rezepttool.manager.services.ImporterServiceException;
import de.tkoehler.rezepttool.manager.services.ManagerServiceException;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/test.properties")
@RunWith(SpringRunner.class)
@DirtiesContext
public class CreateRecipeControllerIntegrationTest {

	@Autowired
	private CreateRecipeController objectUnderTest;

	@SuppressWarnings("unchecked")
	@Test
	public void controllerImportAndSave_load2xDifferentURLs_CorrectIngredNames() throws ImporterServiceException, ManagerServiceException {
		String url1 = "\"https://www.chefkoch.de/rezepte/2280021363771917/Knoblauch-Champignons.html\"";
		String url2 = "\"https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html\"";
		ResponseEntity<RecipeWebInput> response1 = objectUnderTest.loadRecipeFromExternalURL(url1);
		assertThat(HttpStatus.OK, is(response1.getStatusCode()));
		RecipeWebInput recipe1 = response1.getBody();
		objectUnderTest.saveRecipe(recipe1);
		ResponseEntity<RecipeWebInput> response2 = objectUnderTest.loadRecipeFromExternalURL(url2);
		assertThat(HttpStatus.OK, is(response2.getStatusCode()));
		RecipeWebInput recipe2 = response2.getBody();
		assertThat(recipe2.getIngredients(), hasItems(
				hasProperty("name", is("Champignons, kleine frische")),
				hasProperty("name", is("Knoblauchzehe(n)")),
				hasProperty("name", is("Olivenöl")),
				hasProperty("name", is("Rosmarin")),
				hasProperty("name", is("Balsamico")),
				hasProperty("name", is("Salz")),
				hasProperty("name", is("Blattpetersilie, gehackte"))));
	}
}
