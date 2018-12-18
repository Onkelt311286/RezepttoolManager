package de.tkoehler.rezepttool.manager.repositories;

import org.springframework.data.repository.CrudRepository;

import de.tkoehler.rezepttool.manager.repositories.model.AlternativeIngredientName;

public interface AlternativeIngredientNameRepository extends CrudRepository<AlternativeIngredientName, String> {

}
