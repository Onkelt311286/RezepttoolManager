package de.tkoehler.rezepttool.manager.services.test;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.application.mappers.WebInputToRecipeEntityMapper;
import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.services.ImporterServiceException;
import de.tkoehler.rezepttool.manager.services.ImporterServiceImpl;
import de.tkoehler.rezepttool.manager.services.model.Recipe;
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
	private WebInputToRecipeEntityMapper chefkochToRecipeMapperMock;

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
		when(chefkochToRecipeMapperMock.process(any())).thenReturn(new RecipeEntity());
		when(recipeParserMock.parseRecipe(any())).thenReturn(new Recipe());
		final String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		objectUnderTest.loadRecipe(url);
		verify(recipeParserMock, times(1)).parseRecipe(url);
	}

	@Test
	public void loadRecipe_CorrectParameter_mapped1x() throws Exception {
		when(chefkochToRecipeMapperMock.process(any())).thenReturn(new RecipeEntity());
		when(recipeParserMock.parseRecipe(any())).thenReturn(new Recipe());
		final String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		objectUnderTest.loadRecipe(url);
		verify(chefkochToRecipeMapperMock, times(1)).process(any());
	}

	@Test(expected = ImporterServiceException.class)
	public void updateRecipeWithKnownData_NullParameter_throwsImporterServiceException() throws Exception {
		objectUnderTest.updateWebRecipeWithKnownData(null);
	}

	@Test(expected = ImporterServiceException.class)
	public void updateRecipeWithKnownData_ExistingParameter_throwsImporterServiceException() throws Exception {
		RecipeEntity recipe = RecipeEntity.builder()
				.url("testUrl")
				.name("testName")
				.build();
		when(recipeRepositoryMock.findByUrlAndName(recipe.getUrl(), recipe.getName())).thenReturn(new ArrayList<RecipeEntity>(Arrays.asList(recipe)));
		objectUnderTest.updateWebRecipeWithKnownData(recipe);
	}

	@Test
	public void updateRecipeWithKnownData_CorrectParameter_findByUrlAndName1x() throws Exception {
		RecipeEntity recipe = RecipeEntity.builder()
				.url("testUrl")
				.name("testName")
				.build();
		when(recipeRepositoryMock.findByUrlAndName(recipe.getUrl(), recipe.getName())).thenReturn(new ArrayList<>());
		objectUnderTest.updateWebRecipeWithKnownData(recipe);
		verify(recipeRepositoryMock, times(1)).findByUrlAndName(recipe.getUrl(), recipe.getName());
	}

	@Test(expected = ImporterServiceException.class)
	public void updateKnownIngredients_NullParameter_throwsImporterServiceException() throws Exception {
		objectUnderTest.updateWebIngredientWithKnownData(null);
	}
	
	@Test
	public void updateKnownIngredients_anyIngredient_findByAlternativeName1x() throws Exception {
		when(ingredientRepositoryMock.findByAlternativeName(any())).thenReturn(new ArrayList<>());
		RecipeIngredient testRecipeIngredient = RecipeIngredient.builder()
				.id("ID")
				.ingredient(Ingredient.builder()
							.id(UUID.randomUUID().toString())
							.name("")
							.alternativeNames(Stream.of("KnownAlternativeName").collect(Collectors.toSet()))
							.build())
				.build();
		List<RecipeIngredient> testList = Arrays.asList(testRecipeIngredient);
		objectUnderTest.updateWebIngredientWithKnownData(testList);
		verify(ingredientRepositoryMock, times(1)).findByAlternativeName("KnownAlternativeName");
	}

	@Test
	public void updateKnownIngredients_KnownIngredient_UpdatesIngredient() throws Exception {
		Ingredient knownIngredient = Ingredient.builder()
				.id("KnownIngredID")
				.name("KnownIngredName")
				.recipeIngredients(new ArrayList<>())
				.alternativeNames(Stream.of("KnownAlternativeName").collect(Collectors.toSet()))
				.build();
		when(ingredientRepositoryMock.findByAlternativeName(any())).thenReturn(Arrays.asList(knownIngredient));
		
		RecipeIngredient testRecipeIngredient = RecipeIngredient.builder()
				.id("ID")
				.ingredient(Ingredient.builder()
							.id(UUID.randomUUID().toString())
							.name("")
							.alternativeNames(Stream.of("KnownAlternativeName").collect(Collectors.toSet()))
							.build())
				.build();
		objectUnderTest.updateWebIngredientWithKnownData(Arrays.asList(testRecipeIngredient));
		assertThat(testRecipeIngredient.getIngredient(), is(knownIngredient));
		assertThat(testRecipeIngredient.getIngredient().getRecipeIngredients(), hasItem(testRecipeIngredient));
	}
	
	@Test
	public void updateKnownIngredients_UnknownIngredient_KeepsIngredient() throws Exception {
		when(ingredientRepositoryMock.findByAlternativeName(any())).thenReturn(new ArrayList<>());
		
		RecipeIngredient testRecipeIngredient = RecipeIngredient.builder()
				.id("ID")
				.ingredient(Ingredient.builder()
							.id(UUID.randomUUID().toString())
							.name("")
							.alternativeNames(Stream.of("UnknownAlternativeName").collect(Collectors.toSet()))
							.build())
				.build();
		objectUnderTest.updateWebIngredientWithKnownData(Arrays.asList(testRecipeIngredient));
		assertThat(testRecipeIngredient.getIngredient().getName(), is("UnknownAlternativeName"));
	}
	
	@Test
	public void updateKnownIngredients_UnknownIngredient_UpdatesIngredient() throws Exception {
		Ingredient knownIngredient = Ingredient.builder()
				.id("KnownIngredID")
				.name("KnownIngredName")
				.recipeIngredients(new ArrayList<>())
				.alternativeNames(Stream.of("FirstAlternativeName").collect(Collectors.toSet()))
				.build();
		when(ingredientRepositoryMock.findByName(any())).thenReturn(Arrays.asList(knownIngredient));
		
		RecipeIngredient testRecipeIngredient = RecipeIngredient.builder()
				.id("ID")
				.ingredient(Ingredient.builder()
							.id("NewIngredID")
							.name("KnownIngredName")
							.alternativeNames(Stream.of("SecondAlternativeName").collect(Collectors.toSet()))
							.build())
				.build();
		objectUnderTest.updateWebIngredientWithKnownData(Arrays.asList(testRecipeIngredient));
		assertThat(testRecipeIngredient.getIngredient().getId(), is("KnownIngredID"));
		assertThat(testRecipeIngredient.getIngredient().getAlternativeNames(), hasItems("FirstAlternativeName", "SecondAlternativeName"));
		assertThat(testRecipeIngredient.getIngredient().getRecipeIngredients(), hasItem(testRecipeIngredient));
		verify(ingredientRepositoryMock, times(1)).findByName("KnownIngredName");
	}
	
	@Test
	public void updateKnownIngredients_UnknownIngredient_findByName1x() throws Exception {
		when(ingredientRepositoryMock.findByName(any())).thenReturn(new ArrayList<>());
		RecipeIngredient testRecipeIngredient = RecipeIngredient.builder()
				.id("ID")
				.ingredient(Ingredient.builder()
							.id("NewIngredID")
							.name("KnownIngredName")
							.alternativeNames(Stream.of("AlternativeName").collect(Collectors.toSet()))
							.build())
				.build();
		objectUnderTest.updateWebIngredientWithKnownData(Arrays.asList(testRecipeIngredient));
		verify(ingredientRepositoryMock, times(1)).findByName("KnownIngredName");
	}
	
	@Test(expected = ImporterServiceException.class)
	public void saveRecipe_NullParameter_throwsImporterServiceException() throws Exception {
		objectUnderTest.loadRecipe(null);
	}
	
	@Test
	public void saveRecipe_CorrectParameter_save1x() throws Exception {
		RecipeEntity recipe = new RecipeEntity();
		objectUnderTest.saveRecipe(recipe);
		verify(recipeRepositoryMock, times(1)).save(recipe);
	}
}
