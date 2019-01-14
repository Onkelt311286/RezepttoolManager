package de.tkoehler.rezepttool.manager.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;

public interface RecipeRepository extends CrudRepository<RecipeEntity, String> {
	@Override
	List<RecipeEntity> findAll();

	List<RecipeEntity> findByUrlAndName(String url, String name);
}
