package de.tkoehler.rezepttool.manager.application.mappers.test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.application.mappers.RecipeEntityToWebInputMapperImpl;
import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

@RunWith(MockitoJUnitRunner.class)
public class RecipeEntityToWebInputMapperTest {

	@InjectMocks
	RecipeEntityToWebInputMapperImpl objectUnderTest;
	RecipeEntity filledRecipeEntity;

	@Before
	public void setUp() {
		filledRecipeEntity = RecipeEntity.builder().build();
	}
	
	@Test
	public void process_NullParameter_returnsNull() {
		RecipeWebInput recipe = objectUnderTest.process(null);
		assertThat(recipe, is(nullValue()));
	}

	@Test
	public void process_TestParamter_NotNull() {
		RecipeWebInput recipe = objectUnderTest.process(filledRecipeEntity);
		assertThat(recipe, is(not(nullValue())));
	}

	@Test
	public void process_filledTestParamter_filledTestValues() {
		fail("Not yet implemented");
	}
}
