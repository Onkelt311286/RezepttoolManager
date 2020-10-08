package de.tkoehler.rezepttool.manager.services.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;

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
public class ChefkochRecipeV2 extends Recipe {

    @JsonAlias({ "@context" })
    private String context;
    @JsonAlias({ "@type" })
    private String type;
    private String cookTime;
    private String prepTime;
    private String dataPublished;
    private String description;
    private String image;
    private List<String> recipeIngredient;
    private String name;
    private Author author;
    private String recipeInstructions;
    private String recipeYield;
    private AggregateRating aggregateRating;
    private List<String> recipeCategory;

}
