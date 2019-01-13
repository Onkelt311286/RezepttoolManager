package de.tkoehler.rezepttool.manager.web.model;

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
public class RecipeWebInput {

	private String url;
	private String name;
	private String additionalInformation;
	private String portions;

	@Builder.Default
	private List<IngredientWebInput> ingredients = new ArrayList<>();

	private String instructions;
	private String workTime;
	private String cookTime;
	private String restTime;
	private String difficulty;
	private String callories;

	@Builder.Default
	private List<String> categories = new ArrayList<>();
}
