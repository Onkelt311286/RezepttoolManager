package de.tkoehler.rezepttool.manager.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.tkoehler.rezepttool.manager.repositories.model.RecipeEntity;
import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;

public interface RecipeRepository extends CrudRepository<RecipeEntity, String> {
	@Override
	List<RecipeEntity> findAll();

	@Query("select new de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe(r.id,r.name) from RecipeEntity r ")
	List<TinyRecipe> findAllTinies();

	@Query("select new de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe(r.id,r.name) from RecipeEntity r where :category in elements(r.categories)")
	List<TinyRecipe> findAllTiniesByCategory(String category);

	List<RecipeEntity> findByUrlAndName(String url, String name);

	@Query("select category from RecipeEntity r JOIN r.categories category where :id = r.id")
	List<String> findCategoriesById(String id);

	@Query("select ringred.ingredient.name from RecipeEntity r JOIN r.ingredients ringred where :id = r.id")
	List<String> findIngredientsById(String id);
}
