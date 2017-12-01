package io.github.leoniedermeier.iex.example.stock.quote;

import java.time.LocalDateTime;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class QuoteService {

    private final RestTemplate restTemplate;

    public QuoteService(final RestTemplateBuilder builder, @Value("${iextrading.root.uri}") final String rootUri) {
        this.restTemplate = builder.rootUri(rootUri).build();
    }

    @HystrixCommand
    public Quote currentQuote(final String symbol) {
        final String result = this.restTemplate.getForObject("/stock/{symbol}/price", String.class, symbol);
        final Quote quote = new Quote();
        if (NumberUtils.isCreatable(result)) {
            quote.setPrice(NumberUtils.toDouble(result));
            quote.setTime(LocalDateTime.now());
        }
        return quote;
    }

    @HystrixCommand
    public OpenClose openClose(final String symbol) {
        return this.restTemplate.getForObject("/stock/{symbol}/open-close", OpenClose.class, symbol);
    }
}
