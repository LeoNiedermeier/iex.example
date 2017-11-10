package io.github.leoniedermeier.iex.example.stock.company;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CompanyService {

	static final String CACHE_NAME_COMPANY = "Company";
	
	private final RestTemplate restTemplate;

	public CompanyService(final RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	@Cacheable(CACHE_NAME_COMPANY)
	public Company getCompany(String symbol) {
		// /stock/aapl/company
		return this.restTemplate.getForObject("https://api.iextrading.com/1.0/stock/{symbol}/company", Company.class,
				symbol);
	}
}
