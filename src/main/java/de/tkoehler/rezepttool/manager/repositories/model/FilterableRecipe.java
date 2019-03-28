package de.tkoehler.rezepttool.manager.repositories.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FilterableRecipe {
	private String id;
	private String name;
	private List<String> ingredients;
	private List<String> categories;
}
