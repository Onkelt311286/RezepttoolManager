package de.tkoehler.rezepttool.manager.repositories.model;

import javax.persistence.Embeddable;

@Embeddable
public enum Difficulty {
	SIMPEL("simpel"), NORMAL("normal"), PFIFFIG("pfiffig");

	private String bezeichnung;

	Difficulty(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}

	public String toString() {
		return bezeichnung;
	}
}
