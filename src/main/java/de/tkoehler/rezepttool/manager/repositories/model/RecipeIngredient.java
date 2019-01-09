package de.tkoehler.rezepttool.manager.repositories.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tblrecipeingredients")
public class RecipeIngredient {

	@Id
	@Column(length = 36, nullable = false)
	@EqualsAndHashCode.Exclude
	private String id;

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(nullable = false)
	@JsonBackReference
	@ToString.Exclude
	private Recipe recipe;

	@Column(length = 50)
	private String amount;

	@ManyToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(nullable = false)
	@JsonBackReference
	private Ingredient ingredient;
	
	
}
