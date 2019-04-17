package de.tkoehler.rezepttool.manager.restcontroller.test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.tkoehler.rezepttool.manager.repositories.model.FilterableRecipe;
import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.restcontroller.RecipePlannerControllerImpl;
import de.tkoehler.rezepttool.manager.restcontroller.model.DailyPlanWebInput;
import de.tkoehler.rezepttool.manager.restcontroller.model.RecipeWebInput;
import de.tkoehler.rezepttool.manager.services.EditorService;
import de.tkoehler.rezepttool.manager.services.EditorServiceException;
import de.tkoehler.rezepttool.manager.services.ManagerService;
import de.tkoehler.rezepttool.manager.services.PlannerService;
import de.tkoehler.rezepttool.manager.services.PlannerServiceException;

@RunWith(MockitoJUnitRunner.class)
public class RecipePlannerControllerTest {

	@InjectMocks
	private RecipePlannerControllerImpl objectUnderTest;
	@Mock
	private ManagerService managerServiceMock;
	@Mock
	private EditorService editorServiceMock;
	@Mock
	private PlannerService plannerServiceMock;

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

	@Test
	public void loadRecipes_anyParameter_ResultNotNull() {
		TinyRecipe[] recipes = { TinyRecipe.builder().build() };
		ResponseEntity<List<RecipeWebInput>> result = objectUnderTest.loadRecipes(recipes);
		assertThat(result, not(nullValue()));
	}

	@Test
	public void loadRecipes_invalidParameter_ServerError() {
		ResponseEntity<List<RecipeWebInput>> result = objectUnderTest.loadRecipes(null);
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}

	@Test
	public void loadRecipes_anyParameter_load3x() throws EditorServiceException {
		String id1 = "testID1";
		String id2 = "testID2";
		String id3 = "testID3";
		TinyRecipe[] recipes = {
				TinyRecipe.builder().id(id1).build(),
				TinyRecipe.builder().id(id2).build(),
				TinyRecipe.builder().id(id3).build()
		};
		objectUnderTest.loadRecipes(recipes);
		verify(editorServiceMock, times(1)).loadRecipe(id1);
		verify(editorServiceMock, times(1)).loadRecipe(id2);
		verify(editorServiceMock, times(1)).loadRecipe(id3);
	}

	@Test
	public void loadRecipes_validParameter_correctResult() throws EditorServiceException {
		String id1 = "testID1";
		String id2 = "testID2";
		String id3 = "testID3";
		TinyRecipe[] recipes = {
				TinyRecipe.builder().id(id1).build(),
				TinyRecipe.builder().id(id2).build(),
				TinyRecipe.builder().id(id3).build()
		};
		RecipeWebInput recipe1 = RecipeWebInput.builder().id(id1).build();
		RecipeWebInput recipe2 = RecipeWebInput.builder().id(id2).build();
		RecipeWebInput recipe3 = RecipeWebInput.builder().id(id3).build();
		when(editorServiceMock.loadRecipe(id1)).thenReturn(recipe1);
		when(editorServiceMock.loadRecipe(id2)).thenReturn(recipe2);
		when(editorServiceMock.loadRecipe(id3)).thenReturn(recipe3);
		ResponseEntity<List<RecipeWebInput>> result = objectUnderTest.loadRecipes(recipes);
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
		assertThat(result.getBody(), hasItems(recipe1, recipe2, recipe3));
	}

	@Test
	public void checkIngredient_anyParameter_ResultNotNull() {
		String json = "{\"id\":\"28a2a0f7-55f8-4b83-94e1-5fb89f4ca975\",\"present\":true}";
		ResponseEntity<String> result = objectUnderTest.checkIngredient(json);
		assertThat(result, not(nullValue()));
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
	}

	@Test
	public void checkIngredient_invalidParameter_ServerError() {
		ResponseEntity<String> result = objectUnderTest.checkIngredient(null);
		assertThat(result, not(nullValue()));
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}

	@Test
	public void checkIngredient_anyParameter_checkIngredient1x() throws PlannerServiceException {
		String json = "{\"id\":\"testID\",\"present\":true}";
		objectUnderTest.checkIngredient(json);
		verify(plannerServiceMock, times(1)).checkIngredient("testID", true);
	}

	@Test
	public void planRecipe_anyParameter_ResultNotNull() {
		ResponseEntity<String> result = objectUnderTest.planRecipe(DailyPlanWebInput.builder().build());
		assertThat(result, not(nullValue()));
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
	}

	@Test
	public void planRecipe_invalidParameter_ServerError() throws PlannerServiceException {
		ResponseEntity<String> result = objectUnderTest.planRecipe(null);
		assertThat(result, not(nullValue()));
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}

	@Test
	public void planRecipe_validParameter_updatePlan1x() throws PlannerServiceException {
		DailyPlanWebInput plan = DailyPlanWebInput.builder()
				.date(new Date())
				.recipes(Arrays.asList(TinyRecipe.builder().build()))
				.build();
		ResponseEntity<String> result = objectUnderTest.planRecipe(plan);
		verify(plannerServiceMock, times(1)).updatePlan(plan);
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
	}

	@Test
	public void planRecipe_validParameter_deletePlan1x() throws PlannerServiceException {
		DailyPlanWebInput plan = DailyPlanWebInput.builder().date(new Date()).build();
		ResponseEntity<String> result = objectUnderTest.planRecipe(plan);
		verify(plannerServiceMock, times(1)).deletePlan(plan);
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
	}

	@Test
	public void loadPlans_anyParameter_ResultNotNull() {
		ResponseEntity<List<DailyPlanWebInput>> result = objectUnderTest.loadPlans(new ArrayList<>());
		assertThat(result, not(nullValue()));
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
	}

	@Test
	public void loadPlans_invalidParameter_ServerError() throws PlannerServiceException {
		ResponseEntity<List<DailyPlanWebInput>> result = objectUnderTest.loadPlans(null);
		assertThat(result, not(nullValue()));
		assertThat(HttpStatus.INTERNAL_SERVER_ERROR, is(result.getStatusCode()));
	}

	@Test
	public void loadPlans_validParameter_loadPlan1x() throws PlannerServiceException {
		DailyPlanWebInput plan = DailyPlanWebInput.builder()
				.date(new Date())
				.recipes(Arrays.asList(TinyRecipe.builder().build()))
				.build();
		ResponseEntity<List<DailyPlanWebInput>> result = objectUnderTest.loadPlans(Arrays.asList(plan));
		verify(plannerServiceMock, times(1)).loadPlan(plan);
		assertThat(HttpStatus.OK, is(result.getStatusCode()));
	}

	@Test
	public void loadIngredients() {
		fail();
	}
}