package io.github.leoniedermeier.iex.example.stock.company;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import io.github.leoniedermeier.iex.example.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Application.class, MockRestServerRestTemplateCustomizer.class })
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class CompanyController_With_Security_ModuleTest {

    @Autowired
    private WebClient webClient;

    @Autowired
    private MockRestServerRestTemplateCustomizer mockServer;

    @Test
    @WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
    public void test_with_admin() throws Exception {

        this.mockServer.expect(requestTo("https://api.iextrading.com/1.0/stock/abc/company"))
                .andRespond(MockRestResponseCreators.withSuccess(
                        new ClassPathResource("/io/github/leoniedermeier/iex/example/stock/company/company_abc.json"),
                        MediaType.APPLICATION_JSON));

        final HtmlPage page = this.webClient.getPage("/stock/abc/company");
        System.out.println(page.getWebResponse().getContentAsString());
        final DomElement loginForm = page.getElementById("login_form");
        assertThat(loginForm, Matchers.nullValue());

        final HtmlTable table = page.getHtmlElementById("tableRefDataSymbols");

        // first data row
        assertThat(table.getCellAt(0, 0).getTextContent(), equalTo("Symbol"));
        assertThat(table.getCellAt(0, 1).getTextContent(), equalTo("ABC"));

        // second data row
        assertThat(table.getCellAt(1, 0).getTextContent(), equalTo("Company name"));
        assertThat(table.getCellAt(1, 1).getTextContent(), equalTo("ABC companyName"));
    }

    @Test
    public void test_no_user() throws Exception {
        // Kein User: Login Page sollte kommen

        final HtmlPage page = this.webClient.getPage("/stock/abc/company");
        System.out.println(page.getWebResponse().getContentAsString());
        final DomElement loginForm = page.getElementById("login_form");
        assertThat(loginForm, Matchers.notNullValue());
    }
}
