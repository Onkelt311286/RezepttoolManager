package de.tkoehler.rezepttool.manager.repositories;

import org.springframework.data.repository.CrudRepository;

import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

}
