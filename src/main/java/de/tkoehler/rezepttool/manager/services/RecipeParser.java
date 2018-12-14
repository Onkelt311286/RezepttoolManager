package de.tkoehler.rezepttool.manager.services;

import de.tkoehler.rezepttool.manager.services.model.Recipe;

public interface RecipeParser {
	Recipe parseRecipe(String url) throws RecipeParserException;
}