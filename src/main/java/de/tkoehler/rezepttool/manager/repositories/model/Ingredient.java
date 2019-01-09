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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
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
	@Transient
	private String originalName;

	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.REFRESH }, mappedBy = "ingredient")
	@JsonManagedReference
	@Builder.Default
	@ToString.Exclude
	private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

	public void addRecipeIngredient(RecipeIngredient ingredient) {
		ingredient.setIngredient(this);
		recipeIngredients.add(ingredient);
	}

	@ElementCollection
	@CollectionTable(name = "tblalternativenames")
	@Builder.Default
	private Set<String> alternativeNames = new HashSet<>();
}
