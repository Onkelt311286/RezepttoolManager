package de.tkoehler.rezepttool.manager.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.web.model.RecipeWebInput;

public interface RecipeRepository extends CrudRepository<RecipeEntity, String> {
	List<RecipeEntity> findByUrlAndName(String url, String name);
}
