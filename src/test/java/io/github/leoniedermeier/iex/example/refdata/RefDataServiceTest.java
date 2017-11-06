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
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

public class RefDataServiceTest {

	private RefDataService refDataService;
	private MockRestServiceServer mockServer;

	@Before
	public void init() {
		final RestTemplate restTemplate = new RestTemplate();
		this.mockServer = MockRestServiceServer.bindTo(restTemplate).build();
		// the MockRestServiceServer.bindTo sets a MockRequestFactory in the
		// RestTemplate. This has to be used in the RestTemplate of the servie also.
		final RestTemplateBuilder builder = new RestTemplateBuilder().requestFactory(restTemplate.getRequestFactory());
		this.refDataService = new RefDataService(builder);
	}

	@Test
	public void testGetRefDataSymbols_empty_result() {

		this.mockServer.expect(requestTo("https://api.iextrading.com/1.0/ref-data/symbols")).andRespond(withSuccess());

		this.refDataService.getRefDataSymbols();
		this.mockServer.verify();
	}

	@Test
	public void testGetRefDataSymbols() throws Exception {
		this.mockServer.expect(requestTo("https://api.iextrading.com/1.0/ref-data/symbols"))
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
