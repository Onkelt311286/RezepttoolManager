package de.tkoehler.rezepttool.manager.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.tkoehler.rezepttool.manager.repositories.model.AlternativeIngredientName;

public interface AlternativeIngredientNameRepository extends CrudRepository<AlternativeIngredientName, String> {
	List<AlternativeIngredientName> findByName(String name);
}
