package de.tkoehler.rezepttool.manager.services.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PreparationInfo {

	private String prepTime;
	private String cookTime;
	private String restTime;
	private String difficulty;
	private String callories;

}
