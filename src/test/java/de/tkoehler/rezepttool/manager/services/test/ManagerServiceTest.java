package de.tkoehler.rezepttool.manager.services.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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
import de.tkoehler.rezepttool.manager.services.ManagerServiceIDNotFoundException;
import de.tkoehler.rezepttool.manager.services.ManagerServiceImpl;
import de.tkoehler.rezepttool.manager.services.ManagerServiceRecipeExistsException;
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

	@Test(expected = ManagerServiceException.class)
	public void saveRecipe_NullParameter_throwsImporterServiceException() throws Exception {
		objectUnderTest.saveRecipe(null);
	}

	@Test(expected = ManagerServiceRecipeExistsException.class)
	public void saveRecipe_ExistingRecipe_throwsImporterServiceRecipeExistsException() throws Exception {
		RecipeEntity recipe = new RecipeEntity();
		when(recipeRepositoryMock.findByUrlAndName(any(), any())).thenReturn(Arrays.asList(recipe));
		objectUnderTest.saveRecipe(new RecipeWebInput());
	}

	@Test
	public void saveRecipe_CorrectParameter_save1x() throws Exception {
		when(WebInputToRecipeEntityMapperMock.process(any())).thenReturn(new RecipeEntity());
		RecipeWebInput recipe = new RecipeWebInput();
		objectUnderTest.saveRecipe(recipe);
		verify(recipeRepositoryMock, times(1)).save(any());
	}

	@Test
	public void saveRecipe_CorrectParameter_process1x() throws Exception {
		when(WebInputToRecipeEntityMapperMock.process(any())).thenReturn(new RecipeEntity());
		RecipeWebInput recipe = new RecipeWebInput();
		objectUnderTest.saveRecipe(recipe);
		verify(WebInputToRecipeEntityMapperMock, times(1)).process(any());
	}

	@Test
	public void saveRecipe_FilledIngredientList_success() throws Exception {
		RecipeEntity recipeEntity = RecipeEntity.builder()
				.ingredients(Arrays.asList(RecipeIngredient.builder()
						.ingredient(Ingredient.builder()
								.department("")
								.name("")
								.build())
						.build()))
				.build();
		RecipeWebInput recipe = new RecipeWebInput();
		when(ingredientRepositoryMock.findByNameAndDepartment(any(), any())).thenReturn(Optional.empty());
		when(WebInputToRecipeEntityMapperMock.process(recipe)).thenReturn(recipeEntity);
		objectUnderTest.saveRecipe(recipe);
	}

	@Test(expected = ManagerServiceException.class)
	public void updateKnownIngredient_NullParameter_throwsImporterServiceException() throws Exception {
		objectUnderTest.updateKnownIngredient(null);
	}

	@Test
	public void updateKnownIngredient_NotNullParameter_find1x() throws Exception {
		when(ingredientRepositoryMock.findByNameAndDepartment(any(), any())).thenReturn(Optional.empty());
		objectUnderTest.updateKnownIngredient(new Ingredient());
		verify(ingredientRepositoryMock, times(1)).findByNameAndDepartment(any(), any());
	}

	@Test
	public void updateKnownIngredient_UnknownParameter_ParameterNotChanged() throws Exception {
		Ingredient ingredEntity = Ingredient.builder()
				.id("newID")
				.name("newName")
				.department("newDepartment")
				.alternativeNames(Stream.of("OriginalName").collect(Collectors.toSet()))
				.build();
		when(ingredientRepositoryMock.findByNameAndDepartment(any(), any())).thenReturn(Optional.empty());
		objectUnderTest.updateKnownIngredient(new Ingredient());
		assertThat(ingredEntity.getId(), is("newID"));
		assertThat(ingredEntity.getName(), is("newName"));
		assertThat(ingredEntity.getDepartment(), is("newDepartment"));
		assertThat(ingredEntity.getAlternativeNames(), contains("OriginalName"));
	}

	@Test
	public void updateKnownIngredient_KnownParameter_ParameterChanged() throws Exception {
		Ingredient newIngredEntity = Ingredient.builder()
				.id("newID")
				.name("oldName")
				.department("oldDepartment")
				.alternativeNames(Stream.of("newOriginalName").collect(Collectors.toSet()))
				.build();
		Ingredient oldIngredEntity = Ingredient.builder()
				.id("oldID")
				.name("oldName")
				.department("oldDepartment")
				.alternativeNames(Stream.of("oldOriginalName").collect(Collectors.toSet()))
				.build();
		when(ingredientRepositoryMock.findByNameAndDepartment(newIngredEntity.getName(), newIngredEntity.getDepartment())).thenReturn(Optional.of(oldIngredEntity));
		objectUnderTest.updateKnownIngredient(newIngredEntity);
		assertThat(newIngredEntity.getId(), is("oldID"));
		assertThat(newIngredEntity.getName(), is("oldName"));
		assertThat(newIngredEntity.getDepartment(), is("oldDepartment"));
		assertThat(newIngredEntity.getAlternativeNames(), containsInAnyOrder("newOriginalName", "oldOriginalName"));
	}

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
