package de.tkoehler.rezepttool.manager.services;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import de.tkoehler.rezepttool.manager.repositories.DailyPlanRepository;
import de.tkoehler.rezepttool.manager.repositories.IngredientRepository;
import de.tkoehler.rezepttool.manager.repositories.RecipeRepository;
import de.tkoehler.rezepttool.manager.repositories.model.DailyPlan;
import de.tkoehler.rezepttool.manager.repositories.model.Ingredient;
import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import de.tkoehler.rezepttool.manager.restcontroller.model.DailyPlanWebInput;

@Component
public class PlannerServiceImpl implements PlannerService {
	private final DailyPlanRepository dailyPlanRepository;
	private final IngredientRepository ingredientRepository;
	private final RecipeRepository recipeRepository;

	public PlannerServiceImpl(DailyPlanRepository dailyPlanRepository, IngredientRepository ingredientRepository, RecipeRepository recipeRepository) {
		this.dailyPlanRepository = dailyPlanRepository;
		this.ingredientRepository = ingredientRepository;
		this.recipeRepository = recipeRepository;
	}

	@Override
	public void checkIngredient(String id, Boolean value) throws PlannerServiceException {
		checkNullParameter(id);
		checkNullParameter(value);
		Optional<Ingredient> ingredient = ingredientRepository.findById(id);
		if (ingredient.isPresent()) {
			ingredient.get().setPresent(value);
			ingredientRepository.save(ingredient.get());
		}
		else {
			throw new PlannerServiceIDNotFoundException("ID could not be found!");
		}
	}

	@Override
	public void updatePlan(DailyPlanWebInput plan) throws PlannerServiceException {
		checkNullParameter(plan);
		Optional<DailyPlan> planEntity = dailyPlanRepository.findByDate(plan.getDate());
		if (planEntity.isPresent()) {
			planEntity.get().setRecipes(plan.getRecipes().stream().map(r -> recipeRepository.findById(r.getId()).get()).collect(Collectors.toList()));
			dailyPlanRepository.save(planEntity.get());
			System.out.println("saved");
		}
		else {
			DailyPlan newPlan = DailyPlan.builder()
					.id(UUID.randomUUID().toString())
					.date(plan.getDate())
					.recipes(plan.getRecipes().stream().map(r -> recipeRepository.findById(r.getId()).get()).collect(Collectors.toList()))
					.build();
			dailyPlanRepository.save(newPlan);
		}
	}

	@Override
	public void deletePlan(DailyPlanWebInput plan) throws PlannerServiceException {
		checkNullParameter(plan);
		Optional<DailyPlan> planEntity = dailyPlanRepository.findByDate(plan.getDate());
		if (planEntity.isPresent()) {
			dailyPlanRepository.delete(planEntity.get());
		}
	}

	@Override
	public DailyPlanWebInput loadPlan(DailyPlanWebInput plan) throws PlannerServiceException {
		Optional<DailyPlan> planEnitiy = dailyPlanRepository.findByDate(plan.getDate());
		if (planEnitiy.isPresent())
			plan.setRecipes(planEnitiy.get().getRecipes().stream().map(r -> TinyRecipe.builder().id(r.getId()).name(r.getName()).build()).collect(Collectors.toList()));
		return plan;
	}

	private void checkNullParameter(Object parameter) throws PlannerServiceException {
		if (parameter == null) throw new PlannerServiceException("Parameter must not be empty!");
	}
}
