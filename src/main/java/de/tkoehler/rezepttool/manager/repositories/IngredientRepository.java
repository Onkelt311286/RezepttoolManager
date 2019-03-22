package de.tkoehler.rezepttool.manager.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.TinyIngredient;

public interface IngredientRepository extends CrudRepository<Ingredient, String> {

	@Query("select new de.tkoehler.rezepttool.manager.repositories.model.TinyIngredient(i.name, i.department) from Ingredient i")
	List<TinyIngredient> findAllTinies();
	
	@Query("select new de.tkoehler.rezepttool.manager.repositories.model.TinyIngredient(i.name, i.department) from Ingredient i where i.name like %:name%")
	List<TinyIngredient> findAllTiniesByName(String name);

	@Query("select distinct i.name from Ingredient i where i.name like %:name%")
	List<String> findIngredientNamesByName(String name);
	
	@Query("select distinct i.department from Ingredient i where i.department like %:department%")
	List<String> findDepartmentsByName(String department);

	@Query("select new de.tkoehler.rezepttool.manager.repositories.model.TinyIngredient(i.name, i.department) from Ingredient i where i.department like %:department%")
	List<TinyIngredient> findAllTiniesByDepartment(String department);
	
	@Query("select new de.tkoehler.rezepttool.manager.repositories.model.TinyIngredient(i.name, i.department) from Ingredient i where  i.name like %:name% and i.department like %:department%")
	List<TinyIngredient> findAllTiniesByNameAndDepartment(String name, String department);

	@Query("select i from Ingredient i where :searchName in elements(i.alternativeNames)")
	List<Ingredient> findByAlternativeName(String searchName);

	Optional<Ingredient> findByNameAndDepartment(String name, String department);


}
