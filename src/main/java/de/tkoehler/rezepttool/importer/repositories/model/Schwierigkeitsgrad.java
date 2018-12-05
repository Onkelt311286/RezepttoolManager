package de.tkoehler.rezepttool.importer.repositories.model;

public enum Schwierigkeitsgrad {
	SIMPEL("simpel"), NORMAL("normal"), PFIFFIG("pfiffig");
	
	private String bezeichnung;
	
	Schwierigkeitsgrad(String bezeichnung){
		this.bezeichnung = bezeichnung;
	}
	
	public String toString() {
		return bezeichnung;
	}
}
