package de.tkoehler.rezepttool.importer.repositories.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tblrezeptzutat")
public class Rezeptzutat {

	private Rezept rezept;
	private String menge;
	private String einheit;
	private String name;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((einheit == null) ? 0 : einheit.hashCode());
		result = prime * result + ((menge == null) ? 0 : menge.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((rezept == null) ? 0 : rezept.getId().hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rezeptzutat other = (Rezeptzutat) obj;
		if (einheit == null) {
			if (other.einheit != null)
				return false;
		} else if (!einheit.equals(other.einheit))
			return false;
		if (menge == null) {
			if (other.menge != null)
				return false;
		} else if (!menge.equals(other.menge))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (rezept == null) {
			if (other.rezept != null)
				return false;
		} else if (!rezept.getId().equals(other.rezept.getId()))
			return false;
		return true;
	}
	
	
}
