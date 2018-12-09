package de.tkoehler.rezepttool.manager.repositories.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import de.tkoehler.rezepttool.manager.repositories.model.Rezept.RezeptBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "tblrezepte")
public class Zutat {

	@Id
	@Column(length = 36, nullable = false)
	private String id;
	
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.REFRESH }, mappedBy = "name")
	@JsonManagedReference
	@Builder.Default
	private List<Rezeptzutat> zutaten = new ArrayList<>();

	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH,
			CascadeType.REFRESH }, mappedBy = "zutat")
	@JsonManagedReference
	@Builder.Default
	private List<AlternativerZutatenName> alternativNamen = new ArrayList<AlternativerZutatenName>();
}
