package de.tkoehler.rezepttool.manager.repositories;

import org.springframework.data.repository.CrudRepository;

import de.tkoehler.rezepttool.manager.repositories.model.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, String> {

}
