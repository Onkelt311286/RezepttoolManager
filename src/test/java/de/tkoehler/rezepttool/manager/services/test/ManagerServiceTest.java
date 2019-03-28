package de.tkoehler.rezepttool.manager.services.test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
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
}
