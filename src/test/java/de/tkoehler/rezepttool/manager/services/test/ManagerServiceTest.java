package de.tkoehler.rezepttool.manager.services.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
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

import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.FilterableRecipe;
import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.services.ManagerServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class ManagerServiceTest {

	@InjectMocks
	private ManagerServiceImpl objectUnderTest;
	@Mock
	private RecipeRepository recipeRepositoryMock;
	@Mock
	private IngredientRepository ingredientRepositoryMock;

	@Test
	public void findAllTinyRecipes_findAllTinies1x() {
		when(recipeRepositoryMock.findAllTinies()).thenReturn(new ArrayList<TinyRecipe>());
		objectUnderTest.findAllTinyRecipes();
		verify(recipeRepositoryMock, times(1)).findAllTinies();
	}

	@Test
	public void findAllTinyIngredients_findAllTinies1x() {
		objectUnderTest.findAllTinyIngredients();
		verify(ingredientRepositoryMock, times(1)).findAllTinies();
	}

	@Test
	public void findAllTiniesByNameAndDepartment1x() {
		String name = "name";
		String department = "department";
		objectUnderTest.findAllTiniesByNameAndDepartment(name, department);
		verify(ingredientRepositoryMock, times(1)).findAllTiniesByNameAndDepartment(name, department);
	}

	@Test
	public void findAllTiniesByName1x() {
		String name = "name";
		objectUnderTest.findAllTiniesByName(name);
		verify(ingredientRepositoryMock, times(1)).findAllTiniesByName(name);
	}

	@Test
	public void findAllTiniesByDepartment1x() {
		String department = "department";
		objectUnderTest.findAllTiniesByDepartment(department);
		verify(ingredientRepositoryMock, times(1)).findAllTiniesByDepartment(department);
	}

	@Test
	public void findIngredientNamesByName1x() {
		String name = "name";
		when(ingredientRepositoryMock.findIngredientNamesByName(name)).thenReturn(Arrays.asList("ingred1", "ingred2"));
		objectUnderTest.findIngredientNamesByName(name);
		verify(ingredientRepositoryMock, times(1)).findIngredientNamesByName(name);
	}

	@Test
	public void findDepartmentsByName1x() {
		String department = "department";
		when(ingredientRepositoryMock.findDepartmentsByName(department)).thenReturn(Arrays.asList("ingred1", "ingred2"));
		objectUnderTest.findDepartmentsByName(department);
		verify(ingredientRepositoryMock, times(1)).findDepartmentsByName(department);
	}

	@Test
	public void findAllFilterableRecipes_findAllTinies1x() {
		when(recipeRepositoryMock.findAllTinies()).thenReturn(new ArrayList<TinyRecipe>());
		objectUnderTest.findAllFilterableRecipes();
		verify(recipeRepositoryMock, times(1)).findAllTinies();
	}

	@Test
	public void findAllFilterableRecipes_findCategoriesById1x() {
		TinyRecipe recipe = TinyRecipe.builder()
				.id("testID")
				.build();
		when(recipeRepositoryMock.findAllTinies()).thenReturn(Arrays.asList(recipe));
		objectUnderTest.findAllFilterableRecipes();
		verify(recipeRepositoryMock, times(1)).findCategoriesById(recipe.getId());
		verify(recipeRepositoryMock, times(1)).findIngredientsById(recipe.getId());
	}

	@Test
	public void findAllFilterableRecipes_correctResult() {
		List<String> categories = Arrays.asList("cat1", "cat2");
		List<String> ingredientNames = Arrays.asList("ingred1", "ingred2");
		TinyRecipe recipe = TinyRecipe.builder()
				.id("testID")
				.name("testName")
				.build();
		when(recipeRepositoryMock.findAllTinies()).thenReturn(Arrays.asList(recipe));
		when(recipeRepositoryMock.findCategoriesById("testID")).thenReturn(categories);
		when(recipeRepositoryMock.findIngredientsById("testID")).thenReturn(ingredientNames);
		List<FilterableRecipe> result = objectUnderTest.findAllFilterableRecipes();
		assertThat(result, not(is(nullValue())));
		assertThat(result.get(0).getId(), is("testID"));
		assertThat(result.get(0).getName(), is("testName"));
		assertThat(result.get(0).getCategories(), is(categories));
		assertThat(result.get(0).getIngredients(), is(ingredientNames));
	}
}
