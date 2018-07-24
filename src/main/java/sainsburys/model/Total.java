package sainsburys.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model to hold total data.
 */
public class Total {
	
	@JsonProperty("gross")
	private Double gross;
	
	@JsonProperty("vat")
	public Double getVat() {
		return gross != null ? new BigDecimal(gross).subtract(BigDecimal.valueOf(gross / 1.2).setScale(2, BigDecimal.ROUND_HALF_UP)).doubleValue() : null;
	}

	public Total(Double gross) {
		this.gross = gross;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gross == null) ? 0 : gross.hashCode());
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
		Total other = (Total) obj;
		if (gross == null) {
			if (other.gross != null)
				return false;
		} else if (!gross.equals(other.gross))
			return false;
		return true;
	}
	
	
}
