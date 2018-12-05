package de.tkoehler.rezepttool.importer.repositories.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
@Table(name = "tblrezepte")
public class Rezept {
	@Id
	@Column(length = 36, nullable = false)
	private String id;

	@Column(length = 200)
	private String url;

	@Column(length = 100, nullable = false)
	private String name;

	@Column(length = 100)
	private String zusaetzlicheInformationen;

	@Column(length = 10)
	private String portionen;

	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH }, mappedBy = "rezept")
	@JsonManagedReference
	@Builder.Default
	@EqualsAndHashCode.Exclude
	private List<Rezeptzutat> zutaten = new ArrayList<>();
	public void addRezeptzutat(Rezeptzutat rezeptzutat) {
		rezeptzutat.setRezept(this);
		zutaten.add(rezeptzutat);
	}

	@Lob
	private String zubereitung;

	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH }, mappedBy = "rezept")
	private Zeit arbeitszeit;

	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH }, mappedBy = "rezept")
	private Zeit kochzeit;

	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH }, mappedBy = "rezept")
	private Zeit ruhezeit;

	@Column(length = 7)
	private Schwierigkeitsgrad schwierigkeitsgrad;

	@Column(length = 10)
	private String kalorien;

	@ElementCollection
	private List<String> kategorien;

	// Mit dem String findet man ein JSON in der Hauptansicht der Rezeptseite,
	// welches alle Informationen enthält.
	// "@context": "http://schema.org"

}
