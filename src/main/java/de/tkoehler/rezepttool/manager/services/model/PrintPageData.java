package de.tkoehler.rezepttool.manager.services.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class PrintPageData {

	private String title;
	private String additionalInformation;
	private String yield;
	
	@Builder.Default
	private List<ChefkochIngredient> ingredients = new ArrayList<>();
	private String instructions;
}
