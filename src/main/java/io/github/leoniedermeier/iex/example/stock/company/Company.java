package io.github.leoniedermeier.iex.example.stock.company;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * https://iextrading.com/developer/docs/#company
 *
 */
public class Company {

	private String symbol;

	private String companyName;

	private String exchange;

	private String industry;

	private String website;

	private String description;

	@JsonProperty(value = "CEO")
	private String ceo;

	private String issueType;

	private String sector;

	public Company() {
		super();
	}

	public String getCeo() {
		return this.ceo;
	}

	public String getCompanyName() {
		return this.companyName;
	}

	public String getDescription() {
		return this.description;
	}

	public String getExchange() {
		return this.exchange;
	}

	public String getIndustry() {
		return this.industry;
	}

	public String getIssueType() {
		return this.issueType;
	}

	public String getSector() {
		return this.sector;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setCeo(final String ceo) {
		this.ceo = ceo;
	}

	public void setCompanyName(final String companyName) {
		this.companyName = companyName;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public void setExchange(final String exchange) {
		this.exchange = exchange;
	}

	public void setIndustry(final String industry) {
		this.industry = industry;
	}

	public void setIssueType(final String issueType) {
		this.issueType = issueType;
	}

	public void setSector(final String sector) {
		this.sector = sector;
	}

	public void setSymbol(final String symbol) {
		this.symbol = symbol;
	}

	public void setWebsite(final String website) {
		this.website = website;
	}

	@Override
	public String toString() {
		return "Company [symbol=" + this.symbol + ", companyName=" + this.companyName + ", exchange=" + this.exchange
				+ ", industry=" + this.industry + ", website=" + this.website + ", description=" + this.description
				+ ", ceo=" + this.ceo + ", issueType=" + this.issueType + ", sector=" + this.sector + "]";
	}
}
