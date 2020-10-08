package de.tkoehler.rezepttool.manager.services.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AggregateRating {

    @JsonAlias({ "@type" })
	private String type;
	private String ratingValue;
	private String reviewCount;
	private int worstRating;
	private int bestRating;
}
