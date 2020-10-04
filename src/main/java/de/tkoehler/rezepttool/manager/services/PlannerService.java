package de.tkoehler.rezepttool.manager.services;

import de.tkoehler.rezepttool.manager.restcontroller.model.DailyPlanWebInput;
import de.tkoehler.rezepttool.manager.restcontroller.model.GroceryPlan;

public interface PlannerService {
	void checkIngredient(String id, Boolean value) throws PlannerServiceException;

	void updatePlan(DailyPlanWebInput plan) throws PlannerServiceException;

	void deletePlan(DailyPlanWebInput plan) throws PlannerServiceException;

	DailyPlanWebInput loadPlan(DailyPlanWebInput plan) throws PlannerServiceException;

	GroceryPlan loadGroceryIngredients(DailyPlanWebInput[] plans)  throws PlannerServiceException;
}