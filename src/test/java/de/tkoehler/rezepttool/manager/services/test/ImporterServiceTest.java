package de.tkoehler.rezepttool.manager.services.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.application.mappers.ServiceRecipeToRepoRecipeMapper;
import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.Recipe;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
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
	private IngredientRepository ingredientRepositoryMock;
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
	public void loadRecipe_CorrectParameter_parsed1x() throws Exception {
		when(chefkochToRecipeMapperMock.process(any())).thenReturn(new Recipe());
		when(recipeParserMock.parseRecipe(any())).thenReturn(new de.tkoehler.rezepttool.manager.services.model.Recipe());
		final String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		objectUnderTest.loadRecipe(url);
		verify(recipeParserMock, times(1)).parseRecipe(url);
	}

	@Test
	public void loadRecipe_CorrectParameter_mapped1x() throws Exception {
		when(chefkochToRecipeMapperMock.process(any())).thenReturn(new Recipe());
		when(recipeParserMock.parseRecipe(any())).thenReturn(new de.tkoehler.rezepttool.manager.services.model.Recipe());
		final String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		objectUnderTest.loadRecipe(url);
		verify(chefkochToRecipeMapperMock, times(1)).process(any());
	}

	@Test(expected = ImporterServiceException.class)
	public void updateRecipeWithKnownData_NullParameter_throwsImporterServiceException() throws Exception {
		objectUnderTest.updateRecipeWithKnownData(null);
	}

	@Test(expected = ImporterServiceException.class)
	public void updateRecipeWithKnownData_ExistingParameter_throwsImporterServiceException() throws Exception {
		Recipe recipe = Recipe.builder()
				.url("testUrl")
				.name("testName")
				.build();
		when(recipeRepositoryMock.findByUrlAndName(recipe.getUrl(), recipe.getName())).thenReturn(new ArrayList<Recipe>(Arrays.asList(recipe)));
		objectUnderTest.updateRecipeWithKnownData(recipe);
	}

	@Test
	public void updateRecipeWithKnownData_CorrectParameter_findByUrlAndName1x() throws Exception {
		Recipe recipe = Recipe.builder()
				.url("testUrl")
				.name("testName")
				.build();
		when(recipeRepositoryMock.findByUrlAndName(recipe.getUrl(), recipe.getName())).thenReturn(new ArrayList<>());
		objectUnderTest.updateRecipeWithKnownData(recipe);
		verify(recipeRepositoryMock, times(1)).findByUrlAndName(recipe.getUrl(), recipe.getName());
	}

	@Test(expected = ImporterServiceException.class)
	public void updateKnownIngredients_NullParameter_throwsImporterServiceException() throws Exception {
		objectUnderTest.updateKnownIngredients(null);
	}
	
	@Test
	public void updateKnownIngredients_anyIngredient_findByAlternativeName1x() throws Exception {
		when(ingredientRepositoryMock.findByAlternativeName(any())).thenReturn(new ArrayList<>());
		RecipeIngredient testRecipeIngredient = RecipeIngredient.builder()
				.id("ID")
				.ingredient(Ingredient.builder()
							.id(UUID.randomUUID().toString())
							.name("")
							.alternativeNames(Arrays.asList("KnownAlternativeName"))
							.build())
				.build();
		List<RecipeIngredient> testList = Arrays.asList(testRecipeIngredient);
		objectUnderTest.updateKnownIngredients(testList);
		verify(ingredientRepositoryMock, times(1)).findByAlternativeName("KnownAlternativeName");
	}

	@Test
	public void updateKnownIngredients_KnownIngredient_UpdatesIngredient() throws Exception {
		Ingredient knownIngredient = Ingredient.builder()
				.id("KnownIngredID")
				.name("KnownIngredName")
				.ingredients(new ArrayList<>())
				.alternativeNames(Arrays.asList("KnownAlternativeName"))
				.build();
		when(ingredientRepositoryMock.findByAlternativeName(any())).thenReturn(Arrays.asList(knownIngredient));
		
		RecipeIngredient testRecipeIngredient = RecipeIngredient.builder()
				.id("ID")
				.ingredient(Ingredient.builder()
							.id(UUID.randomUUID().toString())
							.name("")
							.alternativeNames(Arrays.asList("KnownAlternativeName"))
							.build())
				.build();
		List<RecipeIngredient> testList = Arrays.asList(testRecipeIngredient);
		objectUnderTest.updateKnownIngredients(testList);
		assertThat(testRecipeIngredient.getIngredient(), is(knownIngredient));
	}
	
	@Test
	public void updateKnownIngredients_UnknownIngredient_KeepsIngredient() throws Exception {
		when(ingredientRepositoryMock.findByAlternativeName(any())).thenReturn(new ArrayList<>());
		
		RecipeIngredient testRecipeIngredient = RecipeIngredient.builder()
				.id("ID")
				.ingredient(Ingredient.builder()
							.id(UUID.randomUUID().toString())
							.name("")
							.alternativeNames(Arrays.asList("UnknownAlternativeName"))
							.build())
				.build();
		List<RecipeIngredient> testList = Arrays.asList(testRecipeIngredient);
		objectUnderTest.updateKnownIngredients(testList);
		assertThat(testRecipeIngredient.getIngredient().getName(), is("UnknownAlternativeName"));
	}
	
	@Test(expected = ImporterServiceException.class)
	public void saveRecipe_NullParameter_throwsImporterServiceException() throws Exception {
		objectUnderTest.loadRecipe(null);
	}
	
	@Test
	public void saveRecipe_CorrectParameter_save1x() throws Exception {
		Recipe recipe = new Recipe();
		objectUnderTest.saveRecipe(recipe);
		verify(recipeRepositoryMock, times(1)).save(recipe);
	}
}
