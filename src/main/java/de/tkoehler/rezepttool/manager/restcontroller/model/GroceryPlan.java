package de.tkoehler.rezepttool.manager.restcontroller.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroceryPlan {
	
	private Date date;
	private List<GroceryRecipe> recipes;

}
