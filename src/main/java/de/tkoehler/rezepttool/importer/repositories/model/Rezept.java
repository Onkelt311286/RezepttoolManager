package de.tkoehler.rezepttool.importer.repositories.model;

import java.util.List;

public class Rezept {

	private String name;
	private String zusaetzlicheInformationen;
	private String portionen;
	private List<Rezeptzutat> zutaten;
	private String zubereitung;
	private Zeit arbeitszeit;
	private Zeit kochzeit;
	private Zeit ruhezeit;
	private Schwierigkeitsgrad schwierigkeitsgrad;
	private String kalorien;
	private List<String> kategorien;
	
//	Mit dem String findet man ein JSON in der Hauptansicht der Rezeptseite, welches alle Informationen enthält.
//	"@context": "http://schema.org"
	
}
