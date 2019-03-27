package de.tkoehler.rezepttool.manager.services.test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.application.mappers.RecipeEntityToWebInputMapper;
import de.tkoehler.rezepttool.manager.application.mappers.WebInputToRecipeEntityMapper;
import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;
import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.services.ManagerServiceException;
import de.tkoehler.rezepttool.manager.services.ManagerServiceImpl;
import de.tkoehler.rezepttool.manager.web.model.IngredientWebInput;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@RunWith(MockitoJUnitRunner.class)
public class ManagerServiceTest {

	@InjectMocks
	private ManagerServiceImpl objectUnderTest;
	@Mock
	private RecipeRepository recipeRepositoryMock;
	@Mock
	private IngredientRepository ingredientRepositoryMock;
	@Mock
	private WebInputToRecipeEntityMapper webInputToRecipeEntityMapperMock;
	@Mock
	private RecipeEntityToWebInputMapper recipeEntityToWebInputMapperMock;

	@Test
	public void showRecipeList_normalCall_findAllTinies1x() {
		when(recipeRepositoryMock.findAllTinies()).thenReturn(new ArrayList<TinyRecipe>());
		objectUnderTest.findAllTinies();
		verify(recipeRepositoryMock, times(1)).findAllTinies();
	}

	@Test
	public void deleteRecipe_correctParameter_deleteByID1x() throws Exception {
		String id = "";
		objectUnderTest.deleteRecipe(id);
		verify(recipeRepositoryMock, times(1)).deleteById(id);
	}
	
	@Test(expected = ManagerServiceException.class)
	public void loadRecipe_nullParameter_throwsManagerServiceException() throws Exception {
		objectUnderTest.loadRecipe(null);
	}
	
	@Test
	public void loadRecipe_correctParameter_findByID1x() throws Exception {
		String id = "";
		RecipeEntity recipe = RecipeEntity.builder().build();
		when(recipeRepositoryMock.findById(id)).thenReturn(Optional.of(recipe));
		objectUnderTest.loadRecipe(id);
		verify(recipeRepositoryMock, times(1)).findById(id);
	}
	
	@Test(expected = ManagerServiceException.class)
	public void loadRecipe_unknownId_throwsManagerServiceException() throws Exception {
		String id = "unknownID";
		when(recipeRepositoryMock.findById(id)).thenReturn(Optional.empty());
		objectUnderTest.loadRecipe(id);
	}
	
	@Test
	public void loadRecipe_knownId_correctResult() throws Exception {
		String id = "knownId";
		RecipeEntity recipe = RecipeEntity.builder().id(id).build();
		RecipeWebInput webRecipe = RecipeWebInput.builder().id(id).build();
		when(recipeRepositoryMock.findById(id)).thenReturn(Optional.of(recipe));
		when(recipeEntityToWebInputMapperMock.process(recipe)).thenReturn(webRecipe);
		RecipeWebInput result = objectUnderTest.loadRecipe(id);
		assertThat(id, is(result.getId()));
	}
	
	@Test(expected = ManagerServiceException.class)
	public void updateRecipe_nullParameter_throwsManagerServiceException() throws ManagerServiceException {
		objectUnderTest.updateRecipe(null);
	}
	
	@Test
	public void updateRecipe_validParameter_processAndSave1x() throws ManagerServiceException {
		String id = "testID";
		RecipeWebInput webRecipe = RecipeWebInput.builder().id(id).build();
		RecipeEntity recipe = RecipeEntity.builder().id(id).build();
		when(recipeRepositoryMock.findById(id)).thenReturn(Optional.of(recipe));
		when(webInputToRecipeEntityMapperMock.process(webRecipe)).thenReturn(recipe);
		objectUnderTest.updateRecipe(webRecipe);
		verify(webInputToRecipeEntityMapperMock, times(1)).process(webRecipe);
		verify(recipeRepositoryMock, times(1)).save(recipe);
	}
	
	@Test(expected = ManagerServiceException.class)
	public void updateRecipe_unknownID_throwsManagerServiceException() throws ManagerServiceException {
		String id = "testID";
		RecipeWebInput webRecipe = RecipeWebInput.builder().id(id).build();
		RecipeEntity recipe = RecipeEntity.builder().id(id).build();
		when(recipeRepositoryMock.findById(id)).thenReturn(Optional.empty());
		objectUnderTest.updateRecipe(webRecipe);
	}
	
	@Test
	public void updateRecipe_unknownIngredients_nothingChanged() throws ManagerServiceException {
		String id = "testID";
		IngredientWebInput webingred = IngredientWebInput.builder()
				.name("testName1")
				.department("testDepartment1")
				.build();
		Ingredient ingred1 = Ingredient.builder()
				.id("oldID")
				.name("testName1")
				.department("testDepartment1")
				.alternativeNames(new HashSet<>())
				.build();
		RecipeIngredient ringred = RecipeIngredient.builder()
				.ingredient(ingred1)
				.build();
		RecipeWebInput webRecipe = RecipeWebInput.builder().id(id).ingredients(Arrays.asList(webingred)).build();
		RecipeEntity recipe = RecipeEntity.builder().id(id).ingredients(Arrays.asList(ringred)).build();
		when(recipeRepositoryMock.findById(id)).thenReturn(Optional.of(recipe));
		when(webInputToRecipeEntityMapperMock.process(webRecipe)).thenReturn(recipe);
		when(ingredientRepositoryMock.findByNameAndDepartment(webingred.getName(), webingred.getDepartment())).thenReturn(Optional.empty());
		objectUnderTest.updateRecipe(webRecipe);
		assertThat("oldID", is(ingred1.getId()));
		assertThat(ingred1.getAlternativeNames().isEmpty(), is(true));
	}
	
	@Test
	public void updateRecipe_knownIngredients_ingredientUpdated() throws ManagerServiceException {
		String id = "testID";
		IngredientWebInput webingred = IngredientWebInput.builder()
				.name("testName1")
				.department("testDepartment1")
				.build();
		Ingredient ingred1 = Ingredient.builder()
				.id("oldID")
				.name("testName1")
				.department("testDepartment1")
				.alternativeNames(new HashSet<>())
				.build();
		RecipeIngredient ringred = RecipeIngredient.builder()
				.ingredient(ingred1)
				.build();
		Ingredient ingred2 = Ingredient.builder()
				.id("otherID")
				.name("testName1")
				.department("testDepartment1")
				.alternativeNames(Stream.of("KnownAlternativeName", "AnotherName").collect(Collectors.toSet()))
				.build();
		RecipeWebInput webRecipe = RecipeWebInput.builder().id(id).ingredients(Arrays.asList(webingred)).build();
		RecipeEntity recipe = RecipeEntity.builder().id(id).ingredients(Arrays.asList(ringred)).build();
		when(recipeRepositoryMock.findById(id)).thenReturn(Optional.of(recipe));
		when(webInputToRecipeEntityMapperMock.process(webRecipe)).thenReturn(recipe);
		when(ingredientRepositoryMock.findByNameAndDepartment(webingred.getName(), webingred.getDepartment())).thenReturn(Optional.of(ingred2));
		objectUnderTest.updateRecipe(webRecipe);
		assertThat("otherID", is(ingred1.getId()));
		assertThat(ingred1.getAlternativeNames(),  hasItems("KnownAlternativeName", "AnotherName"));
	}
}
