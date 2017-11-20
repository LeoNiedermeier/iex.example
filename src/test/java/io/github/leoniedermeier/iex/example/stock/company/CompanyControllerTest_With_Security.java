package io.github.leoniedermeier.iex.example.stock.company;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcWebClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

import io.github.leoniedermeier.iex.example.Application;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@AutoConfigureMockRestServiceServer
@Import({ Application.class, CompanyControllerTest_With_Security.MockConfig.class })

public class CompanyControllerTest_With_Security {

    @Configuration
    public static class MockConfig {

        @Bean
        MockServerRestTemplateCustomizer mockServerRestTemplateCustomizer() {
         return new MockServerRestTemplateCustomizer();   
        }
//        @Bean
//        RestTemplate restTemplate(MockServerRestTemplateCustomizer customizer) {
//            RestTemplate restTemplate = new RestTemplate();
//            customizer.customize(restTemplate);
//            return restTemplate;
//        }
//
//        @Bean
//        RestTemplateBuilder restTemplateBuilder(final RestTemplate restTemplate) {
//
//            return new RestTemplateBuilder().requestFactory(restTemplate.getRequestFactory());
//        }
    }

    @Autowired
    private WebClient webClient;

    @Autowired
    MockServerRestTemplateCustomizer customizer;
   // MockRestServiceServer server;

    @Test
    public void test() throws Exception {
        customizer.getServers();
        customizer.getServer().expect(requestTo("https://api.iextrading.com/1.0/stock/abc/company"))
                .andRespond(withSuccess(
                        new ClassPathResource("/io/github/leoniedermeier/iex/example/stock/company/company_abc.json"),
                        APPLICATION_JSON));

        final HtmlPage page = this.webClient.getPage("/stock/abc/company");
        System.out.println(page.getWebResponse().getContentAsString());
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
