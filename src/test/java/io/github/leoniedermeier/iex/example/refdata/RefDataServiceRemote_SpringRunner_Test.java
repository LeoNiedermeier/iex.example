package io.github.leoniedermeier.iex.example.refdata;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

// wie in 
// https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-rest-client
// beschrieben
@RunWith(SpringRunner.class)
@RestClientTest(RefDataServiceRemote.class)
public class RefDataServiceRemote_SpringRunner_Test {

	@Autowired
	RefDataService refDataService;

	@Autowired
	MockRestServiceServer server;

	@Test
	public void test() {

		this.server.expect(requestTo("https://api.iextrading.com/1.0/ref-data/symbols"))
				.andRespond(withSuccess(
						new ClassPathResource("io/github/leoniedermeier/iex/example/refdata/refDataSymbols.json"),
						APPLICATION_JSON));

		final List<RefDataSymbol> refDataSymbols = this.refDataService.getRefDataSymbols();
		assertThat(refDataSymbols, iterableWithSize(2));

		checkResult(refDataSymbols.get(0), "A", "AGILENT TECHNOLOGIES INC", true, LocalDate.of(2017, 4, 19));

		checkResult(refDataSymbols.get(1), "AA", "ALCOA CORP", true, LocalDate.of(2017, 4, 19));

		this.server.verify();
	}

	private static void checkResult(final RefDataSymbol refDataSymbol, final String symbol, final String name,
			final boolean enabled, final LocalDate data) {
		assertThat(refDataSymbol.getSymbol(), is(symbol));
		assertThat(refDataSymbol.getName(), is(name));
		assertThat(refDataSymbol.getDate(), equalTo(data));
		assertThat(refDataSymbol.isEnabled(), equalTo(enabled));
	}

}
