package de.tkoehler.rezepttool.manager.repositories.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class TinyIngredient {

	private final String name;
	private final String department;

	public TinyIngredient(String name, String department) {
		this.name = name;
		this.department = department;
	}
}
