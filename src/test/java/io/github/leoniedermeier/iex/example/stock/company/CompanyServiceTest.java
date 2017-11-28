package io.github.leoniedermeier.iex.example.stock.company;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.client.MockRestServiceServer;

public class CompanyServiceTest {

    private MockRestServiceServer server;
    private CompanyService companyService;

    @Before
    public void init() {
        // Wie in javadoc von MockServerRestTemplateCustomizer
        final MockServerRestTemplateCustomizer mockServerCustomizer = new MockServerRestTemplateCustomizer();
        this.companyService = new CompanyService(new RestTemplateBuilder(mockServerCustomizer),null);
        this.server = mockServerCustomizer.getServer();
    }

    @Test
    public void testGetCompany() {
        // RootUri wird automatich entfernd
        this.server.expect(requestTo("/stock/abc/company"))
                .andRespond(withSuccess(
                        new ClassPathResource("/io/github/leoniedermeier/iex/example/stock/company/company_abc.json"),
                        APPLICATION_JSON));
        final Company company = this.companyService.getCompany("abc");

        assertThat(company.getSymbol(), equalTo("ABC"));
        assertThat(company.getCompanyName(), equalTo("ABC companyName"));
        assertThat(company.getExchange(), equalTo("ABC exchange"));
        assertThat(company.getIndustry(), equalTo("ABC industry"));
        assertThat(company.getWebsite(), equalTo("ABC website"));
        assertThat(company.getDescription(), equalTo("ABC description"));
        assertThat(company.getCeo(), equalTo("ABC ceo"));
        assertThat(company.getIssueType(), equalTo("ABC issueType"));
        assertThat(company.getSector(), equalTo("ABC sector"));

        this.server.verify();
    }
}
