package de.tkoehler.rezepttool.manager.services.model;

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
@EqualsAndHashCode(callSuper = false)
@Builder
public class ChefkochRecipe extends Recipe {

	private String context;
	private String type;
	private String cookTime;
	private String prepTime;
	private String dataPublished;
	private String description;
	private String image;
	private String[] recipeIngredient;
	private String name;
	private Author author;
	private String recipeInstructions;
	private String recipeYield;
	private AggregateRating aggregateRating;
	private String[] recipeCategory;
}
