package de.tkoehler.rezepttool.manager.restcontroller.test;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.restcontroller.RecipeOverviewControllerImpl;
import de.tkoehler.rezepttool.manager.services.ImporterServiceException;
import de.tkoehler.rezepttool.manager.services.ManagerService;
import de.tkoehler.rezepttool.manager.services.ManagerServiceException;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@RunWith(MockitoJUnitRunner.class)
public class RecipeOverviewControllerTest {

	@InjectMocks
	private RecipeOverviewControllerImpl objectUnderTest;
	@Mock
	private ManagerService managerServiceMock;

	@Test
	public void loadTinyRecipes_NotNull() {
		ResponseEntity<List<TinyRecipe>> result = objectUnderTest.loadTinyRecipes();
		assertThat(result, not(nullValue()));
	}

	@Test
	public void loadTinyRecipes_BodyNotNull() {
		ResponseEntity<List<TinyRecipe>> result = objectUnderTest.loadTinyRecipes();
		assertThat(result.getBody(), not(nullValue()));
	}

	@Test
	public void loadTinyRecipes_BodyNotEmpty() {
		when(managerServiceMock.findAllTinies()).thenReturn(Arrays.asList(TinyRecipe.builder().name("testName").id("testID").build()));
		ResponseEntity<List<TinyRecipe>> result = objectUnderTest.loadTinyRecipes();
		assertThat(result.getBody().isEmpty(), is(false));
	}

	@Test
	public void loadTinyRecipes_findAllTinies1x() {
		when(managerServiceMock.findAllTinies()).thenReturn(Arrays.asList(TinyRecipe.builder().name("testName").id("testID").build()));
		objectUnderTest.loadTinyRecipes();
		verify(managerServiceMock, times(1)).findAllTinies();
	}

	@Test
	public void loadTinyRecipes_correctContent() {
		when(managerServiceMock.findAllTinies()).thenReturn(Arrays.asList(TinyRecipe.builder().name("testName").id("testID").build()));
		ResponseEntity<List<TinyRecipe>> result = objectUnderTest.loadTinyRecipes();
		assertThat(result.getBody().get(0).getName(), is("testName"));
		assertThat(result.getBody().get(0).getId(), is("testID"));
	}

	@Test
	public void deleteRecipe_anyParameter_resultNotNull() {
		ResponseEntity<String> result = objectUnderTest.deleteRecipe("");
		assertThat(result, not(nullValue()));
	}

	@Test
	public void deleteRecipe_invalidParameter_ServerError() throws ManagerServiceException {
		doThrow(new ManagerServiceException()).when(managerServiceMock).deleteRecipe(null);
		ResponseEntity<String> result = objectUnderTest.deleteRecipe(null);
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}

	@Test
	public void deleteRecipe_anyParameter_delete1x() throws ManagerServiceException {
		String id = "testID";
		objectUnderTest.deleteRecipe(id);
		verify(managerServiceMock, times(1)).deleteRecipe(id);
	}

	@Test
	public void deleteRecipe_validParameter_correctResult() throws ManagerServiceException {
		String id = "testID";
		ResponseEntity<String> result = objectUnderTest.deleteRecipe(id);
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
	}

	@Test
	public void loadRecipe_anyParameter_resultNotNull() {
		ResponseEntity<RecipeWebInput> result = objectUnderTest.loadRecipe("");
		assertThat(result, not(nullValue()));
	}

	@Test
	public void loadRecipe_invalidParameter_ServerError() throws ManagerServiceException {
		doThrow(new ManagerServiceException()).when(managerServiceMock).loadRecipe(null);
		ResponseEntity<RecipeWebInput> result = objectUnderTest.loadRecipe(null);
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}

	@Test
	public void loadRecipe_anyParameter_load1x() throws ManagerServiceException {
		String id = "testID";
		objectUnderTest.loadRecipe(id);
		verify(managerServiceMock, times(1)).loadRecipe(id);
	}

	@Test
	public void loadRecipe_validParameter_correctResult() throws ManagerServiceException {
		String id = "testID";
		when(managerServiceMock.loadRecipe(id)).thenReturn(RecipeWebInput.builder().id(id).build());
		ResponseEntity<RecipeWebInput> result = objectUnderTest.loadRecipe(id);
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
		assertThat(id, is(result.getBody().getId()));
	}
	
	@Test
	public void updateRecipe_anyParameter_resultNotNull() {
		RecipeWebInput input = RecipeWebInput.builder().build();
		ResponseEntity<String> result = objectUnderTest.updateRecipe(input);
		assertThat(result, not(nullValue()));
	}
	
	@Test
	public void updateRecipe_NullParameter_ServerError() throws ManagerServiceException {
		doThrow(new ManagerServiceException()).when(managerServiceMock).updateRecipe(null);
		ResponseEntity<String> result = objectUnderTest.updateRecipe(null);
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}
	
	@Test
	public void updateRecipe_validParameter_update1x() throws ManagerServiceException {
		RecipeWebInput input = RecipeWebInput.builder().build();
		objectUnderTest.updateRecipe(input);
		verify(managerServiceMock, times(1)).updateRecipe(input);
	}
	
	@Test
	public void updateRecipe_validParameter_correctResult() throws ManagerServiceException {
		RecipeWebInput input = RecipeWebInput.builder().build();
		ResponseEntity<String> result = objectUnderTest.updateRecipe(input);
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
		assertThat("\"success\"", is(result.getBody()));
	}
}