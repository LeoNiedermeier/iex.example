package io.github.leoniedermeier.iex.example.refdata;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.time.LocalDate;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

public class RefDataServiceRemoteTest {

    private RefDataService refDataService;
    private MockRestServiceServer mockServer;

    @Before
    public void init() {
        // Wie in javadoc von MockServerRestTemplateCustomizer
        MockServerRestTemplateCustomizer customizer = new MockServerRestTemplateCustomizer();
        this.refDataService = new RefDataServiceRemote(new RestTemplateBuilder(customizer), null);
        this.mockServer = customizer.getServer();
    }

    @Test
    public void testGetRefDataSymbols_empty_result() {

        this.mockServer.expect(requestTo("/ref-data/symbols")).andRespond(withSuccess());

        this.refDataService.getRefDataSymbols();
        this.mockServer.verify();
    }

    @Test
    public void testGetRefDataSymbols() throws Exception {
        this.mockServer.expect(requestTo("/ref-data/symbols"))
                .andRespond(withSuccess(
                        new ClassPathResource("io/github/leoniedermeier/iex/example/refdata/refDataSymbols.json"),
                        MediaType.APPLICATION_JSON_UTF8));

        final List<RefDataSymbol> refDataSymbols = this.refDataService.getRefDataSymbols();
        assertThat(refDataSymbols, iterableWithSize(2));

        checkResult(refDataSymbols.get(0), "A", "AGILENT TECHNOLOGIES INC", true, LocalDate.of(2017, 4, 19));

        checkResult(refDataSymbols.get(1), "AA", "ALCOA CORP", true, LocalDate.of(2017, 4, 19));

        this.mockServer.verify();
    }

    private static void checkResult(final RefDataSymbol refDataSymbol, final String symbol, final String name,
            final boolean enabled, final LocalDate data) {
        assertThat(refDataSymbol.getSymbol(), is(symbol));
        assertThat(refDataSymbol.getName(), is(name));
        assertThat(refDataSymbol.getDate(), equalTo(data));
        assertThat(refDataSymbol.isEnabled(), equalTo(enabled));
    }
}
