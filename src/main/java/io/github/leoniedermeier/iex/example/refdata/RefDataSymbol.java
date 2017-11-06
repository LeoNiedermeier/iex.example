package io.github.leoniedermeier.iex.example.refdata;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents data retrieved from <code>/ref-data/symbols</code>.
 *
 */
public class RefDataSymbol {

	/**
	 * Refers to the symbol represented in Nasdaq Integrated symbology (INET).
	 */
	private String symbol;

	/**
	 * Refers to the name of the company or security.
	 */
	private String name;

	/**
	 * Refers to the date the symbol reference data was generated.
	 */
	private LocalDate date;

	/**
	 * Enabled will be true if the symbol is enabled for trading on IEX.
	 * <p>
	 * Property name in Json is <code>isEnabled</code>.
	 * </p>
	 */
	@JsonProperty(value = "isEnabled")
	private boolean enabled;

	public RefDataSymbol() {
		super();
	}

	public RefDataSymbol(final String symbol, final String name) {
		super();
		this.symbol = symbol;
		this.name = name;
		this.enabled = true;
		this.date = LocalDate.now();
	}

	public String getSymbol() {
		return this.symbol;
	}

	public void setSymbol(final String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		return this.date;
	}

	public void setDate(final LocalDate date) {
		this.date = date;
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(final boolean isEnabled) {
		this.enabled = isEnabled;
	}

	@Override
	public String toString() {
		return "RefDataSymbols [symbol=" + this.symbol + ", name=" + this.name + ", date=" + this.date + ", enabled="
				+ this.enabled + "]";
	}
}
