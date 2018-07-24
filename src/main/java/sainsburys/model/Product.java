package sainsburys.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model to hold product data.
 */
public class Product {

	@JsonProperty("title")
	private String title;

	@JsonProperty("kcal_per_100g")
	private Double kCalPer100G;

	@JsonProperty("unit_price")
	private Double pricePerUnit;

	@JsonProperty("description")
	private String description;

	public Product(String title, Double kCalPer100G, Double pricePerUnit, String description) {
		this.title = title;
		this.kCalPer100G = kCalPer100G;
		this.pricePerUnit = pricePerUnit;
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getkCalPer100G() {
		return kCalPer100G;
	}

	public void setkCalPer100G(Double kCalPer100G) {
		this.kCalPer100G = kCalPer100G;
	}

	public Double getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Product [title=" + title + ", kCalPer100G=" + kCalPer100G + ", pricePerUnit=" + pricePerUnit
				+ ", description=" + description + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((kCalPer100G == null) ? 0 : kCalPer100G.hashCode());
		result = prime * result + ((pricePerUnit == null) ? 0 : pricePerUnit.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		Product other = (Product) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (kCalPer100G == null) {
			if (other.kCalPer100G != null)
				return false;
		} else if (!kCalPer100G.equals(other.kCalPer100G))
			return false;
		if (pricePerUnit == null) {
			if (other.pricePerUnit != null)
				return false;
		} else if (!pricePerUnit.equals(other.pricePerUnit))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
