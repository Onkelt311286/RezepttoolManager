package de.tkoehler.rezepttool.manager.services.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.application.mappers.RecipeEntityToWebInputMapper;
import de.tkoehler.rezepttool.manager.application.mappers.WebInputToRecipeEntityMapper;
import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.services.ManagerServiceException;
import de.tkoehler.rezepttool.manager.services.ManagerServiceIDNotFoundException;
import de.tkoehler.rezepttool.manager.services.ManagerServiceImpl;
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
	private WebInputToRecipeEntityMapper WebInputToRecipeEntityMapperMock;
	@Mock
	private RecipeEntityToWebInputMapper recipeEntityToWebInputMapperMock;

	@Test
	public void showRecipeList_normalCall_findAllTinies1x() {
		when(recipeRepositoryMock.findAllTinies()).thenReturn(new ArrayList<TinyRecipe>());
		objectUnderTest.showRecipeList();
		verify(recipeRepositoryMock, times(1)).findAllTinies();
	}

	@Test(expected = ManagerServiceException.class)
	public void editRecipe_nullParameter_throwsImporterServiceException() throws Exception {
		objectUnderTest.editRecipe(null);
	}

	@Test(expected = ManagerServiceIDNotFoundException.class)
	public void editRecipe_emptyParameter_throwsImporterServiceIDNotFoundException() throws Exception {
		when(recipeRepositoryMock.findById(any())).thenReturn(Optional.empty());
		objectUnderTest.editRecipe("");
	}

	@Test
	public void editRecipe_correctParameter_findById1x() throws Exception {
		String id = "testID";
		RecipeEntity recipe = RecipeEntity.builder().build();
		RecipeWebInput webRecipe = RecipeWebInput.builder().build();
		when(recipeRepositoryMock.findById(id)).thenReturn(Optional.of(recipe));
		when(recipeEntityToWebInputMapperMock.process(recipe)).thenReturn(webRecipe);
		objectUnderTest.editRecipe(id);
		verify(recipeRepositoryMock, times(1)).findById(id);
	}

	@Test
	public void editRecipe_correctParameter_process1x() throws Exception {
		String id = "testID";
		RecipeEntity recipe = RecipeEntity.builder().build();
		RecipeWebInput webRecipe = RecipeWebInput.builder().build();
		when(recipeRepositoryMock.findById(id)).thenReturn(Optional.of(recipe));
		when(recipeEntityToWebInputMapperMock.process(recipe)).thenReturn(webRecipe);
		objectUnderTest.editRecipe(id);
		verify(recipeEntityToWebInputMapperMock, times(1)).process(recipe);
	}

	@Test
	public void editRecipe_correctParameter_notNull() throws Exception {
		String id = "testID";
		RecipeEntity recipe = RecipeEntity.builder().build();
		RecipeWebInput webRecipe = RecipeWebInput.builder().build();
		when(recipeRepositoryMock.findById(id)).thenReturn(Optional.of(recipe));
		when(recipeEntityToWebInputMapperMock.process(recipe)).thenReturn(webRecipe);
		RecipeWebInput result = objectUnderTest.editRecipe(id);
		assertThat(result, is(not(nullValue())));
	}

	@Test(expected = ManagerServiceException.class)
	public void deleteRecipe_nullParameter_throwsImporterServiceException() throws Exception {
		objectUnderTest.editRecipe(null);
	}

	@Test
	public void deleteRecipe_correctParameter_deleteByID1x() throws Exception {
		String id = "";
		objectUnderTest.deleteRecipe(id);
		verify(recipeRepositoryMock, times(1)).deleteById(id);
	}
}
