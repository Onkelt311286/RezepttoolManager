package de.tkoehler.rezepttool.manager.application.mappers;

import de.tkoehler.rezepttool.manager.services.model.Recipe;

public interface ServiceRecipeToRepoRecipeMapper {

	de.tkoehler.rezepttool.manager.repositories.model.Recipe process(Recipe recipe);

}