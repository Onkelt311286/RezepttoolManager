package de.tkoehler.rezepttool.manager.services.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrintPageData {

	private String title;
	private String additionalInformation;
	private String yield;

	@Builder.Default
	private List<ChefkochIngredient> ingredients = new ArrayList<>();
	private String instructions;
}
