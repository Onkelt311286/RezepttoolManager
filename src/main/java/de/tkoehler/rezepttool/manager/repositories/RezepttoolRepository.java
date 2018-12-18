package de.tkoehler.rezepttool.manager.repositories;

import org.springframework.data.repository.CrudRepository;

import de.tkoehler.rezepttool.manager.repositories.model.RecipeIngredient;

public interface RezepttoolRepository extends CrudRepository<RecipeIngredient, String> {

}
