
package de.tkoehler.rezepttool.manager.restcontroller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
public class IngredientWebInput {
	private String recipeIngredientId;
	private String ingredientId;
	private String amount;
	private String name;
	private String originalName;
	private String department;
	private String originalDepartment;
	
	private boolean present;
}
