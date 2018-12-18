package de.tkoehler.rezepttool.manager.repositories.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@Entity
@Table(name = "tblingredients", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
public class Ingredient {

	@Id
	@Column(length = 36, nullable = false)
	@EqualsAndHashCode.Exclude
	private String id;

	@Column(length = 100, nullable = false)
	private String name;

	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.REFRESH }, mappedBy = "ingredient")
	@JsonManagedReference
	@Builder.Default
	@ToString.Exclude
	private List<RecipeIngredient> ingredients = new ArrayList<>();

	public void addIngredient(RecipeIngredient ingredient) {
		ingredient.setIngredient(this);
		ingredients.add(ingredient);
	}

	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.REFRESH }, mappedBy = "ingredient")
	@JsonManagedReference
	@Builder.Default
	private List<AlternativeIngredientName> alternativeNames = new ArrayList<>();

	public void addAlternativeName(AlternativeIngredientName alternativeName) {
		alternativeName.setIngredient(this);
		alternativeNames.add(alternativeName);
	}
}
