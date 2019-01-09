package de.tkoehler.rezepttool.manager.services.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AggregateRating {

	private String type;
	private String ratingValue;
	private String reviewCount;
	private int worstRating;
	private int bestRating;
}
