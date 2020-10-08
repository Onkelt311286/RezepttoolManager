package de.tkoehler.rezepttool.manager.services.test;

import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.repositories.DailyPlanRepository;
import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.DailyPlan;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.restcontroller.model.DailyPlanWebInput;
import de.tkoehler.rezepttool.manager.services.PlannerServiceImpl;
import de.tkoehler.rezepttool.manager.services.exceptions.PlannerServiceException;
import de.tkoehler.rezepttool.manager.services.exceptions.PlannerServiceIDNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class PlannerServiceTest {

	@InjectMocks
	private PlannerServiceImpl objectUnderTest;
	@Mock
	private DailyPlanRepository dailyPlanRepositoryMock;
	@Mock
	private IngredientRepository ingredientRepositoryMock;
	@Mock
	private RecipeRepository recipeRepositoryMock;

	@Test(expected = PlannerServiceException.class)
	public void checkIngredient_NullParameter_throwsPlannerServiceException() throws Exception {
		objectUnderTest.checkIngredient(null, null);
	}

	@Test
	public void checkIngredient_validParameter_findById1x() throws Exception {
		String id = "TestID";
		Boolean present = false;
		when(ingredientRepositoryMock.findById(id)).thenReturn(Optional.of(Ingredient.builder().build()));
		objectUnderTest.checkIngredient(id, present);
		verify(ingredientRepositoryMock, times(1)).findById(id);
	}

	@Test
	public void checkIngredient_validParameter_saveFoundIngredient() throws Exception {
		String id = "TestID";
		Boolean present = false;
		Ingredient ingred = Ingredient.builder().id(id).present(true).build();
		when(ingredientRepositoryMock.findById(id)).thenReturn(Optional.of(ingred));
		objectUnderTest.checkIngredient(id, present);
		verify(ingredientRepositoryMock, times(1)).save(ingred);
	}

	@Test
	public void checkIngredient_validParameter_changedPresent() throws Exception {
		String id = "TestID";
		Boolean present = false;
		Ingredient oldIngred = Ingredient.builder().id(id).present(!present).build();
		Ingredient newIngred = Ingredient.builder().id(id).present(present).build();
		when(ingredientRepositoryMock.findById(id)).thenReturn(Optional.of(oldIngred));
		objectUnderTest.checkIngredient(id, present);
		verify(ingredientRepositoryMock, times(1)).save(newIngred);
	}

	@Test(expected = PlannerServiceIDNotFoundException.class)
	public void checkIngredient_unknownParameter_throwsIDNotFoundException() throws Exception {
		String id = "TestID";
		Boolean present = false;
		when(ingredientRepositoryMock.findById(id)).thenReturn(Optional.empty());
		objectUnderTest.checkIngredient(id, present);
	}

	@Test(expected = PlannerServiceException.class)
	public void updatePlan_NullParameter_throwsPlannerServiceException() throws Exception {
		objectUnderTest.updatePlan(null);
	}

	@Test
	public void updatePlan_validParameter_findByDate1x() throws Exception {
		Date now = new Date();
		DailyPlanWebInput webPlan = DailyPlanWebInput.builder()
				.date(now)
				.build();
		objectUnderTest.updatePlan(webPlan);
		verify(dailyPlanRepositoryMock, times(1)).findByDate(now);
	}

	@Test
	public void updatePlan_unknownDate_findRecipeById3x() throws Exception {
		Date now = new Date();
		String id1 = "id1";
		String id2 = "id2";
		String id3 = "id3";
		DailyPlanWebInput webPlan = DailyPlanWebInput.builder()
				.date(now)
				.recipes(Arrays.asList(
						TinyRecipe.builder().id(id1).build(),
						TinyRecipe.builder().id(id2).build(),
						TinyRecipe.builder().id(id3).build()))
				.build();
		when(dailyPlanRepositoryMock.findByDate(now)).thenReturn(Optional.empty());
		when(recipeRepositoryMock.findById(id1)).thenReturn(Optional.of(RecipeEntity.builder().build()));
		when(recipeRepositoryMock.findById(id2)).thenReturn(Optional.of(RecipeEntity.builder().build()));
		when(recipeRepositoryMock.findById(id3)).thenReturn(Optional.of(RecipeEntity.builder().build()));
		objectUnderTest.updatePlan(webPlan);
		verify(recipeRepositoryMock, times(1)).findById(id1);
		verify(recipeRepositoryMock, times(1)).findById(id2);
		verify(recipeRepositoryMock, times(1)).findById(id3);
	}

	@Test
	public void updatePlan_unknownDate_saveRecipe1x() throws Exception {
		Date now = new Date();
		String id1 = "id1";
		String id2 = "id2";
		String id3 = "id3";
		DailyPlanWebInput webPlan = DailyPlanWebInput.builder()
				.date(now)
				.recipes(Arrays.asList(
						TinyRecipe.builder().id(id1).build(),
						TinyRecipe.builder().id(id2).build(),
						TinyRecipe.builder().id(id3).build()))
				.build();
		when(dailyPlanRepositoryMock.findByDate(now)).thenReturn(Optional.empty());
		when(recipeRepositoryMock.findById(id1)).thenReturn(Optional.of(RecipeEntity.builder().build()));
		when(recipeRepositoryMock.findById(id2)).thenReturn(Optional.of(RecipeEntity.builder().build()));
		when(recipeRepositoryMock.findById(id3)).thenReturn(Optional.of(RecipeEntity.builder().build()));
		objectUnderTest.updatePlan(webPlan);
		verify(dailyPlanRepositoryMock, times(1)).save(Mockito.any());
	}

	@Test
	public void updatePlan_unknownDate_correctRecipeData() throws Exception {
		Date now = new Date();
		String id1 = "id1";
		String id2 = "id2";
		String id3 = "id3";
		DailyPlanWebInput webPlan = DailyPlanWebInput.builder()
				.date(now)
				.recipes(Arrays.asList(
						TinyRecipe.builder().id(id1).build(),
						TinyRecipe.builder().id(id2).build(),
						TinyRecipe.builder().id(id3).build()))
				.build();
		RecipeEntity recipe1 = RecipeEntity.builder().id(id1).build();
		RecipeEntity recipe2 = RecipeEntity.builder().id(id2).build();
		RecipeEntity recipe3 = RecipeEntity.builder().id(id3).build();
		DailyPlan entityPlan = DailyPlan.builder()
				.date(now)
				.recipes(Arrays.asList(recipe1, recipe2, recipe3))
				.build();
		when(dailyPlanRepositoryMock.findByDate(now)).thenReturn(Optional.empty());
		when(recipeRepositoryMock.findById(id1)).thenReturn(Optional.of(recipe1));
		when(recipeRepositoryMock.findById(id2)).thenReturn(Optional.of(recipe2));
		when(recipeRepositoryMock.findById(id3)).thenReturn(Optional.of(recipe3));
		objectUnderTest.updatePlan(webPlan);
		verify(dailyPlanRepositoryMock, times(1)).save(entityPlan);
	}

	@Test
	public void updatePlan_knownDate_saveRecipe1x() throws Exception {
		Date now = new Date();
		String id1 = "id1";
		DailyPlanWebInput webPlan = DailyPlanWebInput.builder()
				.date(now)
				.recipes(Arrays.asList(
						TinyRecipe.builder().id(id1).build()))
				.build();
		DailyPlan entityPlan = DailyPlan.builder()
				.id("testID")
				.date(now)
				.recipes(Arrays.asList(RecipeEntity.builder().build()))
				.build();
		when(dailyPlanRepositoryMock.findByDate(now)).thenReturn(Optional.of(entityPlan));
		when(recipeRepositoryMock.findById(id1)).thenReturn(Optional.of(RecipeEntity.builder().build()));
		objectUnderTest.updatePlan(webPlan);
		verify(dailyPlanRepositoryMock, times(1)).save(entityPlan);
	}

	@Test
	public void updatePlan_knownDate_correctRecipeData() throws Exception {
		Date now = new Date();
		String id1 = "id1";
		String id2 = "id2";
		String id3 = "id3";
		String id4 = "id4";
		String planID = "planID";
		DailyPlanWebInput webPlan = DailyPlanWebInput.builder()
				.date(now)
				.recipes(Arrays.asList(
						TinyRecipe.builder().id(id1).build(),
						TinyRecipe.builder().id(id2).build(),
						TinyRecipe.builder().id(id3).build()))
				.build();
		RecipeEntity recipe1 = RecipeEntity.builder().id(id1).build();
		RecipeEntity recipe2 = RecipeEntity.builder().id(id2).build();
		RecipeEntity recipe3 = RecipeEntity.builder().id(id3).build();
		RecipeEntity recipe4 = RecipeEntity.builder().id(id4).build();
		DailyPlan entityPlan = DailyPlan.builder()
				.id(planID)
				.date(now)
				.recipes(Arrays.asList(recipe1, recipe4))
				.build();
		DailyPlan updatedPlan = DailyPlan.builder()
				.id(planID)
				.date(now)
				.recipes(Arrays.asList(recipe1, recipe2, recipe3))
				.build();
		when(dailyPlanRepositoryMock.findByDate(now)).thenReturn(Optional.of(entityPlan));
		when(recipeRepositoryMock.findById(id1)).thenReturn(Optional.of(recipe1));
		when(recipeRepositoryMock.findById(id2)).thenReturn(Optional.of(recipe2));
		when(recipeRepositoryMock.findById(id3)).thenReturn(Optional.of(recipe3));
		objectUnderTest.updatePlan(webPlan);
		verify(dailyPlanRepositoryMock, times(1)).save(updatedPlan);
	}

	@Test(expected = PlannerServiceException.class)
	public void deletePlan_nullParameter_throwsPlannerServiceException() throws PlannerServiceException {
		objectUnderTest.deletePlan(null);
	}
	
	@Test
	public void deletePlan_unkownParameter_success() throws PlannerServiceException {
		Date now = new Date();
		DailyPlanWebInput plan = DailyPlanWebInput.builder().date(now).build();
		DailyPlan foundPlan = DailyPlan.builder().date(now).build();
		when(dailyPlanRepositoryMock.findByDate(now)).thenReturn(Optional.empty());
		objectUnderTest.deletePlan(plan);
		verify(dailyPlanRepositoryMock, times(0)).delete(foundPlan);
	}
	
	@Test
	public void deletePlan_kownParameter_delete1x() throws PlannerServiceException {
		Date now = new Date();
		DailyPlanWebInput plan = DailyPlanWebInput.builder().date(now).build();
		DailyPlan foundPlan = DailyPlan.builder().date(now).build();
		when(dailyPlanRepositoryMock.findByDate(now)).thenReturn(Optional.of(foundPlan));
		objectUnderTest.deletePlan(plan);
		verify(dailyPlanRepositoryMock, times(1)).delete(foundPlan);
	}

	@Test(expected = PlannerServiceException.class)
	public void loadPlan_nullParameter_throwsPlannerServiceException() throws PlannerServiceException {
		objectUnderTest.loadPlan(null);
	}
	
	@Test
	public void loadPlan_unkownParameter_returnIdentity() throws PlannerServiceException {
		Date now = new Date();
		DailyPlanWebInput plan = DailyPlanWebInput.builder().date(now).build();
		when(dailyPlanRepositoryMock.findByDate(now)).thenReturn(Optional.empty());
		DailyPlanWebInput result = objectUnderTest.loadPlan(plan);
		assertThat(result, is(plan));
	}
	
	@Test
	public void loadPlan_kownParameter_updateParamter() throws PlannerServiceException {
		Date now = new Date();
		DailyPlanWebInput plan = DailyPlanWebInput.builder().date(now).build();
		RecipeEntity foundRecipe = RecipeEntity.builder().name("testRecipeName").build();
		RecipeEntity foundRecipe2 = RecipeEntity.builder().name("testRecipeName2").build();
		DailyPlan foundPlan = DailyPlan.builder()
				.date(now)
				.recipes(Arrays.asList(foundRecipe, foundRecipe2))
				.build();
		when(dailyPlanRepositoryMock.findByDate(now)).thenReturn(Optional.of(foundPlan));
		DailyPlanWebInput result = objectUnderTest.loadPlan(plan);
		assertThat(result.getRecipes().get(0).getName(), anyOf(is(foundRecipe.getName()), is(foundRecipe2.getName())));
	}
	
	@Test
	public void loadGroceryIngredients() {
		fail();
	}
}
