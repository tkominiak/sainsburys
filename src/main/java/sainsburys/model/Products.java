package sainsburys.model;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model to hold products and total data.
 */
public class Products {

	@JsonProperty("results")
	private ArrayList<Product> produkts;

	@JsonProperty("total")
	private Total total;

	public Products(ArrayList<Product> produkts) {
		this.produkts = produkts;
		Double gross = produkts.stream().mapToDouble(Product::getPricePerUnit).sum();
		this.total = new Total(gross);
	}

	public ArrayList<Product> getProdukts() {
		return produkts;
	}

	public Total getTotal() {
		return total;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((produkts == null) ? 0 : produkts.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
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
		Products other = (Products) obj;
		if (produkts == null) {
			if (other.produkts != null)
				return false;
		} else if (!produkts.equals(other.produkts))
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		return true;
	}
	
}
