package de.tkoehler.rezepttool.manager.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tkoehler.rezepttool.manager.repositories.model.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, String> {
	List<Recipe> findByUrlAndName(String url, String name);
}
