package de.tkoehler.rezepttool.manager.restcontroller.test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
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
import org.springframework.http.ResponseEntity;

import de.tkoehler.rezepttool.manager.repositories.model.FilterableRecipe;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.restcontroller.RecipePlannerControllerImpl;
import de.tkoehler.rezepttool.manager.services.ManagerService;

@RunWith(MockitoJUnitRunner.class)
public class RecipePlannerControllerTest {

	@InjectMocks
	private RecipePlannerControllerImpl objectUnderTest;
	@Mock
	private ManagerService managerServiceMock;

	@Test
	public void loadTinyRecipes_NotNull() {
		ResponseEntity<List<FilterableRecipe>> result = objectUnderTest.loadFilterableRecipes();
		assertThat(result, not(nullValue()));
	}

	@Test
	public void loadTinyRecipes_BodyNotNull() {
		ResponseEntity<List<FilterableRecipe>> result = objectUnderTest.loadFilterableRecipes();
		assertThat(result.getBody(), not(nullValue()));
	}

	@Test
	public void loadTinyRecipes_BodyNotEmpty() {
		when(managerServiceMock.findAllFilterableRecipes()).thenReturn(Arrays.asList(FilterableRecipe.builder().build()));
		ResponseEntity<List<FilterableRecipe>> result = objectUnderTest.loadFilterableRecipes();
		assertThat(result.getBody().isEmpty(), is(false));
	}

	@Test
	public void loadTinyRecipes_findAllTinies1x() {
		when(managerServiceMock.findAllFilterableRecipes()).thenReturn(Arrays.asList(FilterableRecipe.builder().build()));
		objectUnderTest.loadFilterableRecipes();
		verify(managerServiceMock, times(1)).findAllFilterableRecipes();
	}

	@Test
	public void loadTinyRecipes_correctContent() {
		FilterableRecipe recipe = FilterableRecipe.builder()
				.name("testName")
				.id("testID")
				.ingredients(Arrays.asList("testIngred1", "testIngred2"))
				.categories(Arrays.asList("cat1", "cat2"))
				.build();
		FilterableRecipe recipe2 = FilterableRecipe.builder()
				.name("testName2")
				.id("testID2")
				.ingredients(Arrays.asList("testIngred3", "testIngred2"))
				.categories(Arrays.asList("cat1", "cat3"))
				.build();
		when(managerServiceMock.findAllFilterableRecipes()).thenReturn(Arrays.asList(recipe, recipe2));
		ResponseEntity<List<FilterableRecipe>> result = objectUnderTest.loadFilterableRecipes();
		assertThat(result.getBody(), hasItems(recipe, recipe2));
	}
}