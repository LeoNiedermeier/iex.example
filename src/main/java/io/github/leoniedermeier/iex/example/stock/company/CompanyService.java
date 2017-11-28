package io.github.leoniedermeier.iex.example.stock.company;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CompanyService {

    static final String CACHE_NAME_COMPANY = "Company";

    private final RestTemplate restTemplate;

    public CompanyService(final RestTemplateBuilder restTemplateBuilder,
            @Value("${iextrading.root.uri}") final String rootUri) {
        this.restTemplate = restTemplateBuilder.rootUri(rootUri).build();
    }

    @Cacheable(CACHE_NAME_COMPANY)
    public Company getCompany(final String symbol) {
        // /stock/aapl/company
        return this.restTemplate.getForObject("/stock/{symbol}/company", Company.class, symbol);
    }

    RestTemplate getRestTemplate() {
        return this.restTemplate;
    }
}
