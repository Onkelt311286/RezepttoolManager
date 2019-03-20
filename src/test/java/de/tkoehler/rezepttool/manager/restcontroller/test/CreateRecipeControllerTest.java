package de.tkoehler.rezepttool.manager.restcontroller.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.tkoehler.rezepttool.manager.restcontroller.CreateRecipeControllerImpl;
import de.tkoehler.rezepttool.manager.services.ImporterService;
import de.tkoehler.rezepttool.manager.services.ImporterServiceException;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@RunWith(MockitoJUnitRunner.class)
public class CreateRecipeControllerTest {

	@InjectMocks
	private CreateRecipeControllerImpl objectUnderTest;
	@Mock
	private ImporterService importerServiceMock;

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
		doThrow(new ImporterServiceException()).when(importerServiceMock).loadRecipe(any());
		ResponseEntity<RecipeWebInput> result = objectUnderTest.loadRecipeFromExternalURL("1234");
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}
	
	@Test
	public void loadRecipeFromExternalURL_CorrectParameter_LoadRecipe1x() throws ImporterServiceException {
		String json = "\"https://www.website.de/test.html\"";
		String value = "https://www.website.de/test.html";
		when(importerServiceMock.loadRecipe(any())).thenReturn(new RecipeWebInput());
		objectUnderTest.loadRecipeFromExternalURL(json);
		verify(importerServiceMock, times(1)).loadRecipe(value);
	}
	
	@Test
	public void loadRecipeFromExternalURL_CorrectParameter_success() throws ImporterServiceException {
		String json = "\"https://www.website.de/test.html\"";
		String value = "https://www.website.de/test.html";
		when(importerServiceMock.loadRecipe(any())).thenReturn(RecipeWebInput.builder().url(value).build());
		ResponseEntity<RecipeWebInput> result = objectUnderTest.loadRecipeFromExternalURL(json);
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
		assertThat(value, is(result.getBody().getUrl()));
	}
	
	@Test
	public void saveRecipe_NullParameter_ServerError() throws ImporterServiceException {
		doThrow(new ImporterServiceException()).when(importerServiceMock).importRecipe(null);
		ResponseEntity<String> result = objectUnderTest.saveRecipe(null);
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}
	
	@Test
	public void saveRecipe_EmptyParameter_ServerError() throws ImporterServiceException {
		doThrow(new ImporterServiceException()).when(importerServiceMock).importRecipe(null);
		ResponseEntity<String> result = objectUnderTest.saveRecipe(null);
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}
	
	@Test
	public void saveRecipe_CorrectParameter_import1x() throws ImporterServiceException {
		RecipeWebInput newRecipe = RecipeWebInput.builder().build();
		objectUnderTest.saveRecipe(newRecipe);
		verify(importerServiceMock, times(1)).importRecipe(newRecipe);
	}
	
	@Test
	public void saveRecipe_RecipeWithEmptyID_success() {
		RecipeWebInput newRecipe = RecipeWebInput.builder().id("").build();
		ResponseEntity<String> result = objectUnderTest.saveRecipe(newRecipe);
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
		assertThat("", not(is(result.getBody().toString())));
	}
}