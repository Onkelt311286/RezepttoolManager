package de.tkoehler.rezepttool.manager.services.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.application.mappers.ServiceRecipeToRepoRecipeMapper;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Recipe;
import de.tkoehler.rezepttool.manager.services.ImporterServiceException;
import de.tkoehler.rezepttool.manager.services.ImporterServiceImpl;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParser;
import de.tkoehler.rezepttool.manager.services.recipeparser.RecipeParserException;

@RunWith(MockitoJUnitRunner.class)
public class ImporterServiceTest {

	@InjectMocks
	private ImporterServiceImpl objectUnderTest;
	@Mock
	private RecipeParser recipeParserMock;
	@Mock
	private RecipeRepository recipeRepositoryMock;
	@Mock
	private ServiceRecipeToRepoRecipeMapper chefkochToRecipeMapperMock;

	@Test(expected = ImporterServiceException.class)
	public void loadRecipe_NullParameter_throwsImporterServiceException() throws Exception {
		objectUnderTest.loadRecipe(null);
	}

	@Test(expected = ImporterServiceException.class)
	public void loadRecipe_WrongParameter_throwsImporterServiceException() throws Exception {
		doThrow(new RecipeParserException()).when(recipeParserMock).parseRecipe(any());
		objectUnderTest.loadRecipe("");
	}

	@Test
	public void loadRecipe_CorrectParameter_NotNull() throws Exception {
		when(chefkochToRecipeMapperMock.process(any())).thenReturn(new Recipe());
		when(recipeParserMock.parseRecipe(any())).thenReturn(new de.tkoehler.rezepttool.manager.services.model.Recipe());
		final String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		objectUnderTest.loadRecipe(url);
		verify(recipeParserMock, times(1)).parseRecipe(url);
	}
}
