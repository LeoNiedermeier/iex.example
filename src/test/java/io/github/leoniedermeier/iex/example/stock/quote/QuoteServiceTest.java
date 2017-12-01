package io.github.leoniedermeier.iex.example.stock.quote;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestToUriTemplate;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

public class QuoteServiceTest {

    private QuoteService quoteService;
    private MockRestServiceServer server;

    @Before
    public void init() {
        final MockServerRestTemplateCustomizer customizer = new MockServerRestTemplateCustomizer();
        this.quoteService = new QuoteService(new RestTemplateBuilder(customizer), "");
        this.server = customizer.getServer();
    }

    @Test
    public void testCurrentQuote() {

        this.server.expect(requestToUriTemplate("/stock/{symbol}/price", "xyz"))
                // ist kein json, sondern nur eine Zahl
                .andRespond(withSuccess("123.12", MediaType.TEXT_PLAIN));
        final Quote currentQuote = this.quoteService.currentQuote("xyz");
        assertThat(currentQuote.getPrice(), comparesEqualTo(123.12));
    }

    @Test
    public void testOpenClose() {
        this.server.expect(requestToUriTemplate("/stock/{symbol}/open-close", "xyz"))
                .andRespond(withSuccess(
                        new ClassPathResource("io/github/leoniedermeier/iex/example/stock/quote/openClose.json"),
                        APPLICATION_JSON));

        final OpenClose openClose = this.quoteService.openClose("xyz");

        // Double mit Fehlerintervall
        Quote open = openClose.getOpen();
        assertThat(open.getPrice(), closeTo(100, 0));
        assertThat(open.getTime(), equalTo(LocalDateTime.of(2017, 10, 11, 15, 30)));

        Quote close = openClose.getClose();
        assertThat(close.getPrice(), closeTo(200, 0));
        assertThat(close.getTime(), equalTo(LocalDateTime.of(2017, 10, 11, 16, 30)));

    }

}
