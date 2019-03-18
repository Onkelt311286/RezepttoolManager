package de.tkoehler.rezepttool.manager.restcontroller.test;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import de.tkoehler.rezepttool.manager.restcontroller.CreateRecipeControllerImpl;

@RunWith(MockitoJUnitRunner.class)
public class CreateRecipeControllerTest {

	@InjectMocks
	private CreateRecipeControllerImpl objectUnderTest;

	@Test
	public void controllerTest() {
		objectUnderTest.loadRecipe("");
		fail("Not yet implemented");
	}
}
