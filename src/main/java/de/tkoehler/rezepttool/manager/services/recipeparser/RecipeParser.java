package de.tkoehler.rezepttool.manager.services.recipeparser;

import de.tkoehler.rezepttool.manager.services.model.Recipe;

public interface RecipeParser {
    Recipe parseRecipe(String url) throws RecipeParserException;
}