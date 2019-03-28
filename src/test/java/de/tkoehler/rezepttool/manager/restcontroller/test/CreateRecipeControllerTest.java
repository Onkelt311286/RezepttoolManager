package de.tkoehler.rezepttool.manager.restcontroller.test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.tkoehler.rezepttool.manager.repositories.model.TinyIngredient;
import de.tkoehler.rezepttool.manager.restcontroller.CreateRecipeControllerImpl;
import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;
import de.tkoehler.rezepttool.manager.services.EditorService;
import de.tkoehler.rezepttool.manager.services.EditorServiceException;
import de.tkoehler.rezepttool.manager.services.ImporterService;
import de.tkoehler.rezepttool.manager.services.ImporterServiceException;
import de.tkoehler.rezepttool.manager.services.ManagerService;

@RunWith(MockitoJUnitRunner.class)
public class CreateRecipeControllerTest {

	@InjectMocks
	private CreateRecipeControllerImpl objectUnderTest;
	@Mock
	private ManagerService managerServiceMock;
	@Mock
	private ImporterService importerServiceMock;
	@Mock
	private EditorService editorServiceMock;

	@Test
	public void loadTinyIngredients_NotNull() {
		ResponseEntity<List<TinyIngredient>> result = objectUnderTest.loadTinyIngredients();
		assertThat(result, not(nullValue()));
	}

	@Test
	public void loadTinyIngredients_BodyNotNull() {
		when(managerServiceMock.findAllTinyIngredients()).thenReturn(new ArrayList<>());
		ResponseEntity<List<TinyIngredient>> result = objectUnderTest.loadTinyIngredients();
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
		assertThat(result.getBody(), not(nullValue()));
		assertThat(result.getBody().isEmpty(), is(true));
	}

	@Test
	public void loadTinyIngredients_loadFromRepo1x() {
		List<TinyIngredient> ingredientList = new ArrayList<>();
		when(managerServiceMock.findAllTinyIngredients()).thenReturn(ingredientList);
		ResponseEntity<List<TinyIngredient>> result = objectUnderTest.loadTinyIngredients();
		verify(managerServiceMock, times(1)).findAllTinyIngredients();
		assertThat(result.getBody(), is(ingredientList));
	}

	@Test
	public void loadTinyIngredients_success() {
		TinyIngredient ingred1 = TinyIngredient.builder()
				.name("testName1")
				.department("testDepartment1")
				.build();
		TinyIngredient ingred2 = TinyIngredient.builder()
				.name("testName2")
				.department("testDepartment2")
				.build();
		List<TinyIngredient> ingredientList = Arrays.asList(ingred1, ingred2);
		when(managerServiceMock.findAllTinyIngredients()).thenReturn(ingredientList);
		ResponseEntity<List<TinyIngredient>> result = objectUnderTest.loadTinyIngredients();
		verify(managerServiceMock, times(1)).findAllTinyIngredients();
		assertThat(result.getBody().isEmpty(), is(false));
		assertThat(result.getBody(), hasItems(ingred1, ingred2));
	}

	@Test
	public void searchIngredients_NullParameter_ResultWithNoFilter() {
		when(managerServiceMock.findAllTinyIngredients()).thenReturn(new ArrayList<>());
		ResponseEntity<List<TinyIngredient>> result = objectUnderTest.searchIngredients(null, null);
		verify(managerServiceMock, times(0)).findAllTiniesByName(any());
		verify(managerServiceMock, times(0)).findAllTiniesByDepartment(any());
		verify(managerServiceMock, times(0)).findAllTiniesByNameAndDepartment(any(), any());
		verify(managerServiceMock, times(1)).findAllTinyIngredients();
		assertThat(result.getBody(), not(nullValue()));
	}

	@Test
	public void searchIngredients_NameParameter_NameFilterResult() {
		TinyIngredient ingred1 = TinyIngredient.builder()
				.name("testName1")
				.department("testDepartment1")
				.build();
		TinyIngredient ingred2 = TinyIngredient.builder()
				.name("testName2")
				.department("testDepartment2")
				.build();
		List<TinyIngredient> ingredients = Arrays.asList(ingred1, ingred2);
		when(managerServiceMock.findAllTiniesByName(any())).thenReturn(ingredients);
		String testInput = "testInput";
		ResponseEntity<List<TinyIngredient>> result = objectUnderTest.searchIngredients(testInput, null);
		verify(managerServiceMock, times(1)).findAllTiniesByName(testInput);
		verify(managerServiceMock, times(0)).findAllTiniesByDepartment(any());
		verify(managerServiceMock, times(0)).findAllTiniesByNameAndDepartment(any(), any());
		verify(managerServiceMock, times(0)).findAllTinyIngredients();
		assertThat(result.getBody(), hasItems(ingred1, ingred2));
	}

	@Test
	public void searchIngredients_DepartmentParameter_DepartmentFilterResult() {
		TinyIngredient ingred1 = TinyIngredient.builder()
				.name("testName1")
				.department("testDepartment1")
				.build();
		TinyIngredient ingred2 = TinyIngredient.builder()
				.name("testName2")
				.department("testDepartment2")
				.build();
		List<TinyIngredient> ingredients = Arrays.asList(ingred1, ingred2);
		when(managerServiceMock.findAllTiniesByDepartment(any())).thenReturn(ingredients);
		String testInput = "testInput";
		ResponseEntity<List<TinyIngredient>> result = objectUnderTest.searchIngredients(null, testInput);
		verify(managerServiceMock, times(0)).findAllTiniesByName(any());
		verify(managerServiceMock, times(1)).findAllTiniesByDepartment(testInput);
		verify(managerServiceMock, times(0)).findAllTiniesByNameAndDepartment(any(), any());
		verify(managerServiceMock, times(0)).findAllTinyIngredients();
		assertThat(result.getBody(), hasItems(ingred1, ingred2));
	}

	@Test
	public void searchIngredients_NameAndDepartmentParameter_NameAndDepartmentFilterResult() {
		TinyIngredient ingred1 = TinyIngredient.builder()
				.name("testName1")
				.department("testDepartment1")
				.build();
		TinyIngredient ingred2 = TinyIngredient.builder()
				.name("testName2")
				.department("testDepartment2")
				.build();
		List<TinyIngredient> ingredients = Arrays.asList(ingred1, ingred2);
		when(managerServiceMock.findAllTiniesByNameAndDepartment(any(), any())).thenReturn(ingredients);
		String testName = "testName";
		String testDepartment = "testDepartment";
		ResponseEntity<List<TinyIngredient>> result = objectUnderTest.searchIngredients(testName, testDepartment);
		verify(managerServiceMock, times(0)).findAllTiniesByName(any());
		verify(managerServiceMock, times(0)).findAllTiniesByDepartment(any());
		verify(managerServiceMock, times(1)).findAllTiniesByNameAndDepartment(testName, testDepartment);
		verify(managerServiceMock, times(0)).findAllTinyIngredients();
		assertThat(result.getBody(), hasItems(ingred1, ingred2));
	}

	@Test
	public void autoCompleteIngredients_NullParameter_ServerError() {
		ResponseEntity<List<TinyIngredient>> result = objectUnderTest.autoCompleteIngredients(null, null);
		assertThat(result.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@Test
	public void autoCompleteIngredients_NameParameter_NameFilterResult() {
		when(managerServiceMock.findIngredientNamesByName(any())).thenReturn(Arrays.asList(TinyIngredient.builder().name("testName").department("testName").build()));
		String testInput = "test";
		ResponseEntity<List<TinyIngredient>> result = objectUnderTest.autoCompleteIngredients(testInput, null);
		assertThat(result.getStatusCode(), is(HttpStatus.OK));
		verify(managerServiceMock, times(1)).findIngredientNamesByName(testInput);
		verify(managerServiceMock, times(0)).findDepartmentsByName(any());
		assertThat("testName", is(result.getBody().get(0).getName()));
		assertThat("testName", is(result.getBody().get(0).getDepartment()));
	}

	@Test
	public void autoCompleteIngredients_DepartmentParameter_DepartmentFilterResult() {
		when(managerServiceMock.findDepartmentsByName(any())).thenReturn(Arrays.asList(TinyIngredient.builder().name("testDepartment").department("testDepartment").build()));
		String testInput = "test";
		ResponseEntity<List<TinyIngredient>> result = objectUnderTest.autoCompleteIngredients(null, testInput);
		assertThat(result.getStatusCode(), is(HttpStatus.OK));
		verify(managerServiceMock, times(0)).findIngredientNamesByName(testInput);
		verify(managerServiceMock, times(1)).findDepartmentsByName(any());
		assertThat(result.getBody().get(0).getName(), is("testDepartment"));
		assertThat(result.getBody().get(0).getDepartment(), is("testDepartment"));
	}

	@Test
	public void autoCompleteIngredients_NameAndDepartmentParameter_ServerError() {
		ResponseEntity<List<TinyIngredient>> result = objectUnderTest.autoCompleteIngredients("test", "test");
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}

	@Test
	public void loadRecipeFromExternalURL_NullParameter_ServerError() {
		ResponseEntity<RecipeWebInput> result = objectUnderTest.loadRecipeFromExternalURL(null);
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}

	@Test
	public void loadRecipeFromExternalURL_EmptyParameter_ServerError() {
		ResponseEntity<RecipeWebInput> result = objectUnderTest.loadRecipeFromExternalURL("");
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}

	@Test
	public void loadRecipeFromExternalURL_WrongParameter_ServerError() throws ImporterServiceException {
		doThrow(new ImporterServiceException()).when(importerServiceMock).loadRecipeFromExternal(any());
		ResponseEntity<RecipeWebInput> result = objectUnderTest.loadRecipeFromExternalURL("1234");
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}

	@Test
	public void loadRecipeFromExternalURL_CorrectParameter_LoadRecipe1x() throws ImporterServiceException {
		String json = "\"https://www.website.de/test.html\"";
		String value = "https://www.website.de/test.html";
		when(importerServiceMock.loadRecipeFromExternal(any())).thenReturn(new RecipeWebInput());
		objectUnderTest.loadRecipeFromExternalURL(json);
		verify(importerServiceMock, times(1)).loadRecipeFromExternal(value);
	}

	@Test
	public void loadRecipeFromExternalURL_CorrectParameter_success() throws ImporterServiceException {
		String json = "\"https://www.website.de/test.html\"";
		String value = "https://www.website.de/test.html";
		when(importerServiceMock.loadRecipeFromExternal(any())).thenReturn(RecipeWebInput.builder().url(value).build());
		ResponseEntity<RecipeWebInput> result = objectUnderTest.loadRecipeFromExternalURL(json);
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
		assertThat(result.getBody().getUrl(), is(value));
	}

	@Test
	public void saveRecipe_NullParameter_ServerError() throws EditorServiceException {
		doThrow(new EditorServiceException()).when(editorServiceMock).insertRecipe(null);
		ResponseEntity<String> result = objectUnderTest.saveRecipe(null);
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}

	@Test
	public void saveRecipe_EmptyParameter_ServerError() throws EditorServiceException {
		doThrow(new EditorServiceException()).when(editorServiceMock).insertRecipe(null);
		ResponseEntity<String> result = objectUnderTest.saveRecipe(null);
		assertThat(result.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
	}

	@Test
	public void saveRecipe_CorrectParameter_import1x() throws EditorServiceException {
		RecipeWebInput newRecipe = RecipeWebInput.builder().build();
		objectUnderTest.saveRecipe(newRecipe);
		verify(editorServiceMock, times(1)).insertRecipe(newRecipe);
	}

	@Test
	public void saveRecipe_RecipeWithEmptyID_success() {
		RecipeWebInput newRecipe = RecipeWebInput.builder().id("").build();
		ResponseEntity<String> result = objectUnderTest.saveRecipe(newRecipe);
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
		assertThat("\"success\"", is(result.getBody().toString()));
	}
}