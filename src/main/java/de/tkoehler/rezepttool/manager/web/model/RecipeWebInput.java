package de.tkoehler.rezepttool.manager.web.model;

import java.util.ArrayList;
import java.util.List;

import de.tkoehler.rezepttool.manager.repositories.model.Difficulty;
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
	private Difficulty difficulty;
	private String callories;

	@Builder.Default
	private List<String> categories = new ArrayList<>();

	public void setDifficulty(String difficulty) {
		switch (difficulty) {
		case "normal":
			this.difficulty = Difficulty.NORMAL;
			break;
		case "pfiffig":
			this.difficulty = Difficulty.PFIFFIG;
			break;
		case "simpel":
		default:
			this.difficulty = Difficulty.SIMPEL;
			break;
		}
	}
}
