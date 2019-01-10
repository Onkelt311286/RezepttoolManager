package de.tkoehler.rezepttool.manager.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
public class IngredientWebInput {
	private String amount;
	private String name;
	private String originalName;
	private String department;
}
