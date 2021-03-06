package de.tkoehler.rezepttool.manager.repositories.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tblrecipes", uniqueConstraints = { @UniqueConstraint(columnNames = { "url", "name" }) })
public class RecipeEntity {
	@Id
	@Column(length = 36, nullable = false)
	@EqualsAndHashCode.Exclude
	private String id;

	@Column(length = 200, nullable = false)
	private String url;

	@Column(length = 100, nullable = false)
	private String name;

	@Column(length = 100)
	private String additionalInformation;

	@Column(length = 10)
	private String portions;

	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.REFRESH, CascadeType.REMOVE }, mappedBy = "recipe")
	@JsonManagedReference
	@Builder.Default
	private List<RecipeIngredient> ingredients = new ArrayList<>();

	public void addRecipeIngredient(RecipeIngredient recipeIngredient) {
		recipeIngredient.setRecipe(this);
		ingredients.add(recipeIngredient);
	}

	@Lob
	private String instructions;

	@Column(length = 20)
	private String workTime;

	@Column(length = 20)
	private String cookTime;

	@Column(length = 20)
	private String restTime;

	// @Enumerated(EnumType.STRING)
	@Column(length = 20)
	private String difficulty;

	@Column(length = 15)
	private String callories;

	@ElementCollection
	@CollectionTable(name = "tblrecipecategories")
	@Column(name = "category")
	@Builder.Default
	private Set<String> categories = new HashSet<>();

	// public void setDifficulty(String difficulty) {
	// switch (difficulty.toLowerCase()) {
	// case "normal":
	// this.difficulty = Difficulty.NORMAL;
	// break;
	// case "pfiffig":
	// this.difficulty = Difficulty.PFIFFIG;
	// break;
	// case "simpel":
	// default:
	// this.difficulty = Difficulty.SIMPEL;
	// break;
	// }
	// }
}
