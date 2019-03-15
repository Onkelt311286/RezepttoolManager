package de.tkoehler.rezepttool.manager.services.test;

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

import de.tkoehler.rezepttool.manager.services.ImporterServiceException;
import de.tkoehler.rezepttool.manager.services.ImporterServiceImpl;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("/test.properties")
@RunWith(SpringRunner.class)
@DirtiesContext
public class ImporterServiceIntegrationTest {

	@Autowired
	private ImporterServiceImpl importerService;

	@Test
	public void serviceImportRecipe_ExistingURL_CorrectIngredCount() throws ImporterServiceException {
		String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		RecipeWebInput recipe = importerService.importRecipe(url);
		assertThat(recipe.getIngredients().size(), is(7));
	}
}
