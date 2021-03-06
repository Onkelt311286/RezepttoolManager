package de.tkoehler.rezepttool.manager.repositories.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class TinyRecipe {

	private final String id;
	private final String name;

	public TinyRecipe(String id, String name) {
		this.id = id;
		this.name = name;
	}
}
