package de.tkoehler.rezepttool.manager.repositories.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@Entity
@Table(name = "tblingredients", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "department" }) })
public class Ingredient {

	@Id
	@Column(length = 36, nullable = false)
	@EqualsAndHashCode.Exclude
	private String id;

	@Column(length = 100, nullable = false)
	private String name;

	@ElementCollection
	@CollectionTable(name = "tblalternativenames")
	@Column(name = "alternativeName")
	@Builder.Default
	private Set<String> alternativeNames = new HashSet<>();

	@Column(length = 100)
	private String department;
}
