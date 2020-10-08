package de.tkoehler.rezepttool.manager.services.test;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.application.mappers.ExternalRecipeToWebInputMapper;
import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.restcontroller.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;
import de.tkoehler.rezepttool.manager.services.ImporterServiceImpl;
import de.tkoehler.rezepttool.manager.services.exceptions.ImporterServiceException;
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
	private IngredientRepository ingredientRepositoryMock;
	@Mock
	private ExternalRecipeToWebInputMapper chefkochToWebInputMapperMock;

	@Test(expected = ImporterServiceException.class)
	public void loadRecipe_NullParameter_throwsImporterServiceException() throws Exception {
		objectUnderTest.loadRecipeFromExternal(null);
	}

	@Test(expected = ImporterServiceException.class)
	public void loadRecipe_WrongParameter_throwsImporterServiceException() throws Exception {
		doThrow(new RecipeParserException()).when(recipeParserMock).parseRecipe(any());
		objectUnderTest.loadRecipeFromExternal("");
	}

	@Test
	public void loadRecipe_CorrectParameter_parsed1x() throws Exception {
		when(chefkochToWebInputMapperMock.process(any())).thenReturn(new RecipeWebInput());
		when(recipeParserMock.parseRecipe(any())).thenReturn(new Recipe());
		final String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		objectUnderTest.loadRecipeFromExternal(url);
		verify(recipeParserMock, times(1)).parseRecipe(url);
	}

	@Test
	public void loadRecipe_CorrectParameter_mapped1x() throws Exception {
		when(chefkochToWebInputMapperMock.process(any())).thenReturn(new RecipeWebInput());
		when(recipeParserMock.parseRecipe(any())).thenReturn(new Recipe());
		final String url = "https://www.chefkoch.de/rezepte/556631153485020/Antipasti-marinierte-Champignons.html";
		objectUnderTest.loadRecipeFromExternal(url);
		verify(chefkochToWebInputMapperMock, times(1)).process(any());
	}

	@Test(expected = ImporterServiceException.class)
	public void updateRecipeWithKnownData_NullParameter_throwsImporterServiceException()
			throws Exception {
		final String url = "TestUrl";
		when(recipeParserMock.parseRecipe(any())).thenReturn(new Recipe());
		when(chefkochToWebInputMapperMock.process(any())).thenReturn(null);
		objectUnderTest.loadRecipeFromExternal(url);
	}

	@Test
	public void updateRecipeWithKnownData_EmptyIngredientList_success() throws Exception {
		final String url = "TestUrl";
		RecipeWebInput recipe = RecipeWebInput.builder()
				.url("testUrl")
				.name("testName")
				.ingredients(new ArrayList<>())
				.build();
		when(recipeParserMock.parseRecipe(any())).thenReturn(new Recipe());
		when(chefkochToWebInputMapperMock.process(any())).thenReturn(recipe);
		objectUnderTest.loadRecipeFromExternal(url);
	}

	@Test
	public void updateRecipeWithKnownData_FilledIngredientList_success() throws Exception {
		final String url = "TestUrl";
		RecipeWebInput recipe = RecipeWebInput.builder()
				.url("testUrl")
				.name("testName")
				.ingredients(Arrays.asList(IngredientWebInput.builder().build()))
				.build();
		when(recipeParserMock.parseRecipe(any())).thenReturn(new Recipe());
		when(chefkochToWebInputMapperMock.process(any())).thenReturn(recipe);
		when(ingredientRepositoryMock.findByAlternativeName(any())).thenReturn(new ArrayList<>());
		objectUnderTest.loadRecipeFromExternal(url);
	}

	@Test
	public void updateWebIngredientWithKnownData_anyIngredient_findByAlternativeName1x()
			throws Exception {
		final String url = "TestUrl";
		IngredientWebInput testWebIngredient = IngredientWebInput.builder()
				.name("KnownAlternativeName")
				.originalName("KnownAlternativeName")
				.build();
		RecipeWebInput recipe = RecipeWebInput.builder()
				.url("testUrl")
				.name("testName")
				.ingredients(Arrays.asList(testWebIngredient))
				.build();
		when(recipeParserMock.parseRecipe(any())).thenReturn(new Recipe());
		when(chefkochToWebInputMapperMock.process(any())).thenReturn(recipe);
		when(ingredientRepositoryMock.findByAlternativeName(any())).thenReturn(new ArrayList<>());
		objectUnderTest.loadRecipeFromExternal(url);
		verify(ingredientRepositoryMock, times(1)).findByAlternativeName("KnownAlternativeName");
	}

	@Test
	public void updateWebIngredientWithKnownData_unknownIngredient_noChanges() throws Exception {
		final String url = "TestUrl";
		IngredientWebInput testWebIngredient = IngredientWebInput.builder()
				.name("UnknownName")
				.originalName("OriginalName")
				.department("")
				.build();
		RecipeWebInput recipe = RecipeWebInput.builder()
				.url("testUrl")
				.name("testName")
				.ingredients(Arrays.asList(testWebIngredient))
				.build();
		when(recipeParserMock.parseRecipe(any())).thenReturn(new Recipe());
		when(chefkochToWebInputMapperMock.process(any())).thenReturn(recipe);
		when(ingredientRepositoryMock.findByAlternativeName(testWebIngredient.getOriginalName())).thenReturn(new ArrayList<>());
		objectUnderTest.loadRecipeFromExternal(url);
		assertThat(testWebIngredient.getName(), is("UnknownName"));
		assertThat(testWebIngredient.getOriginalName(), is("OriginalName"));
		assertThat(testWebIngredient.getDepartment(), is(""));
	}

	@Test
	public void updateWebIngredientWithKnownData_KnownIngredient1x_UpdatesIngredient() throws Exception {
		final String url = "TestUrl";
		IngredientWebInput testWebIngredient = IngredientWebInput.builder()
				.name("KnownAlternativeName")
				.originalName("KnownAlternativeName")
				.department("")
				.build();
		RecipeWebInput recipe = RecipeWebInput.builder()
				.url("testUrl")
				.name("testName")
				.ingredients(Arrays.asList(testWebIngredient))
				.build();
		Ingredient knownIngredient = Ingredient.builder()
				.id("KnownIngredID")
				.name("KnownIngredName")
				.department("knownDepartment")
				.alternativeNames(Stream.of("KnownAlternativeName", "AnotherName").collect(Collectors.toSet()))
				.build();
		when(recipeParserMock.parseRecipe(any())).thenReturn(new Recipe());
		when(chefkochToWebInputMapperMock.process(any())).thenReturn(recipe);
		when(ingredientRepositoryMock.findByAlternativeName(testWebIngredient.getOriginalName())).thenReturn(Arrays.asList(knownIngredient));
		objectUnderTest.loadRecipeFromExternal(url);
		assertThat(testWebIngredient.getName(), is(knownIngredient.getName()));
		assertThat(testWebIngredient.getOriginalName(), is("KnownAlternativeName"));
		assertThat(testWebIngredient.getDepartment(), is(knownIngredient.getDepartment()));
	}

	@Test
	public void updateWebIngredientWithKnownData_KnownIngredient2x_UpdatesIngredientWithMultipleNames() throws Exception {
		final String url = "TestUrl";
		IngredientWebInput testWebIngredient = IngredientWebInput.builder()
				.name("KnownAlternativeName")
				.originalName("KnownAlternativeName")
				.department("")
				.build();
		RecipeWebInput recipe = RecipeWebInput.builder()
				.url("testUrl")
				.name("testName")
				.ingredients(Arrays.asList(testWebIngredient))
				.build();
		Ingredient knownIngredient = Ingredient.builder()
				.id("KnownIngredID")
				.name("KnownIngredName")
				.department("knownDepartment")
				.alternativeNames(Stream.of("KnownAlternativeName", "AnotherName1").collect(Collectors.toSet()))
				.build();
		Ingredient knownIngredient2 = Ingredient.builder()
				.id("KnownIngredID2")
				.name("KnownIngredName2")
				.department("knownDepartment2")
				.alternativeNames(Stream.of("KnownAlternativeName", "AnotherName2").collect(Collectors.toSet()))
				.build();
		when(recipeParserMock.parseRecipe(any())).thenReturn(new Recipe());
		when(chefkochToWebInputMapperMock.process(any())).thenReturn(recipe);
		when(ingredientRepositoryMock.findByAlternativeName(testWebIngredient.getOriginalName())).thenReturn(Arrays.asList(knownIngredient, knownIngredient2));
		objectUnderTest.loadRecipeFromExternal(url);
		assertThat(testWebIngredient.getName(), anyOf(is("KnownIngredName | KnownIngredName2"), is("KnownIngredName2 | KnownIngredName")));
		assertThat(testWebIngredient.getOriginalName(), is("KnownAlternativeName"));
		assertThat(testWebIngredient.getDepartment(), anyOf(is("knownDepartment | knownDepartment2"), is("knownDepartment2 | knownDepartment")));
	}

	@Test
	public void updateWebIngredientWithKnownData_unknownIngredient2x_noChanges() throws Exception {
		final String url = "TestUrl";
		IngredientWebInput testWebIngredient = IngredientWebInput.builder()
				.name("KnownAlternativeName")
				.originalName("KnownAlternativeName")
				.department("")
				.build();
		RecipeWebInput recipe = RecipeWebInput.builder()
				.url("testUrl")
				.name("testName")
				.ingredients(Arrays.asList(testWebIngredient))
				.build();
		Ingredient knownIngredient = Ingredient.builder()
				.id("KnownIngredID")
				.name("KnownIngredName")
				.department("KnownDepartment")
				.build();
		Ingredient knownIngredient2 = Ingredient.builder()
				.id("KnownIngredID2")
				.name("KnownIngredName")
				.department("KnownDepartment")
				.build();
		when(recipeParserMock.parseRecipe(any())).thenReturn(new Recipe());
		when(chefkochToWebInputMapperMock.process(any())).thenReturn(recipe);
		when(ingredientRepositoryMock.findByAlternativeName(testWebIngredient.getOriginalName())).thenReturn(Arrays.asList(knownIngredient, knownIngredient2));
		objectUnderTest.loadRecipeFromExternal(url);
		assertThat(testWebIngredient.getName(), is("KnownIngredName"));
		assertThat(testWebIngredient.getDepartment(), is("KnownDepartment"));
	}
}
