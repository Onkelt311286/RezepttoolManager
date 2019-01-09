package de.tkoehler.rezepttool.manager.services.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Builder
public class ChefkochRecipe extends Recipe {

	private String url;
	private String context;
	private String type;
	private String cookTime;
	private String prepTime;
	private String dataPublished;
	private String description;
	private String image;
	private String[] recipeIngredients;
	private String name;
	private String additionalInformation;
	private Author author;
	private String recipeInstructions;
	private String recipeYield;
	private AggregateRating aggregateRating;
	private String[] recipeCategories;
	private PreparationInfo preparationInfo;
	private PrintPageData printPageData;

}
