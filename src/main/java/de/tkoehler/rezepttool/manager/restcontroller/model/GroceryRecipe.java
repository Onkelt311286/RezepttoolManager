package de.tkoehler.rezepttool.manager.restcontroller.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroceryRecipe {

	private String name;
	private List<IngredientWebInput> ingredients;
	
}
