package de.tkoehler.rezepttool.manager.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

	@Query("select i from Ingredient i where name in elements(i.alternativeNames)")
	List<Ingredient> findByAlternativeName(String name);
	
	List<Ingredient> findByName(String name);
}
