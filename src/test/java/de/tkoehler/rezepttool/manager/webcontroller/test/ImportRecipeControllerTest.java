package de.tkoehler.rezepttool.manager.webcontroller.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.services.ImporterService;
import de.tkoehler.rezepttool.manager.web.controller.ImportRecipeController;

@RunWith(MockitoJUnitRunner.class)
public class ImportRecipeControllerTest {
	
	@InjectMocks
	private ImportRecipeController importerRecipeController;
	@Mock
	private RecipeRepository recipeRepositoryMock;
	@Mock
	private ImporterService importerServiceMock;
	
	@Test
	public void saveRecipe_() {
		fail("Not yet implemented");
	}

}
