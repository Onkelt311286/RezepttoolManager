package de.tkoehler.rezepttool.manager.webcontroller.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.services.ManagerService;
import de.tkoehler.rezepttool.manager.web.controller.OldCreateRecipeController;

@RunWith(MockitoJUnitRunner.class)
public class ImportRecipeControllerTest {
	
	@InjectMocks
	private OldCreateRecipeController importerRecipeController;
	@Mock
	private RecipeRepository recipeRepositoryMock;
	@Mock
	private ManagerService importerServiceMock;
	
	@Test
	public void saveRecipe_duplicateIngredient_savesNoDuplicate() {
		fail("Not yet implemented");
	}

}
