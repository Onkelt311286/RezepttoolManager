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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import de.tkoehler.rezepttool.manager.application.mappers.WebInputToRecipeEntityMapperImpl;
import de.tkoehler.rezepttool.manager.services.ImporterServiceException;
import de.tkoehler.rezepttool.manager.services.ImporterServiceImpl;
import de.tkoehler.rezepttool.manager.services.ManagerServiceException;
import de.tkoehler.rezepttool.manager.services.ManagerServiceImpl;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@RunWith(SpringRunner.class)
public class CreateRecipeControllerTest {

	@Autowired
	private ImporterServiceImpl importerService;
	@Autowired
	private ManagerServiceImpl managerService;
	@Autowired
	private WebInputToRecipeEntityMapperImpl mapper;

	@SuppressWarnings("unchecked")
	@Test
	public void controllerImportAndSave_load2xDifferentURLs_CorrectIngredNames() throws ImporterServiceException, ManagerServiceException {
		String url1 = "https://www.chefkoch.de/rezepte/2280021363771917/Knoblauch-Champignons.html";
		String url2 = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		RecipeWebInput recipe1 = importerService.importRecipe(url1);
		managerService.saveRecipe(recipe1);
		RecipeWebInput recipe2 = importerService.importRecipe(url2);
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
