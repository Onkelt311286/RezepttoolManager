package de.tkoehler.rezepttool.manager.restcontroller.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.tkoehler.rezepttool.manager.repositories.model.TinyRecipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyPlanWebInput {
	private Date date;

	@Builder.Default
	private List<TinyRecipe> recipes = new ArrayList<>();
}
