package io.github.leoniedermeier.iex.example.refdata;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

/**
 * 
 * https://docs.spring.io/spring-boot/docs/2.0.x/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications-testing-autoconfigured-mvc-tests
 *
 * Hinweis:
 * Mit diesem Setup wird auch die Autokonfiguration von Spring Boot verwendet. "Per Hand" wäre der Setup ziemlich aufwändig.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { RefDataController.class })
public class RefDataController_SpringRunner_Test {

	@Autowired
	private WebClient webClient;

	@MockBean
	private RefDataService refDataService;

	@Test
	public void testRefdata() throws Exception {
		when(refDataService.getRefDataSymbols())
				.thenReturn(asList(new RefDataSymbol("a-symbol", "a-name"), new RefDataSymbol("b-symbol", "b-name")));

		HtmlPage page = this.webClient.getPage("/refdata");
		final HtmlTable table = page.getHtmlElementById("tableRefDataSymbols");

		// header row ignored

		// first data row
		assertThat(table.getCellAt(1, 0).getTextContent(), equalTo("a-name"));
		assertThat(table.getCellAt(1, 1).getTextContent(), equalTo("a-symbol"));

		// second data row
		assertThat(table.getCellAt(2, 0).getTextContent(), equalTo("b-name"));
		assertThat(table.getCellAt(2, 1).getTextContent(), equalTo("b-symbol"));
	}

}
