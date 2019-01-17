package de.tkoehler.rezepttool.manager.repositories.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TinyRecipe {

	private final String id;
	private final String name;

	public TinyRecipe(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
