package io.github.leoniedermeier.iex.example.stock.news;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.web.client.MockServerRestTemplateCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

public class NewsServiceTest {

    private MockRestServiceServer server;

    private NewsService newsService;

    @Before
    public void init() {

        final MockServerRestTemplateCustomizer customizer = new MockServerRestTemplateCustomizer();
        this.newsService = new NewsService(new RestTemplateBuilder(customizer),null);
        this.server = customizer.getServer();
    }

    @Test
    public void testGetNews() {
        this.server.expect(MockRestRequestMatchers.requestTo("/stock/aapl/news/last/5"))
                .andRespond(MockRestResponseCreators.withSuccess(
                        new ClassPathResource("io/github/leoniedermeier/iex/example/stock/news/news.json"),
                        MediaType.APPLICATION_JSON));

        final List<News> news = this.newsService.getNews("aapl", Integer.valueOf(5));
        assertThat(news, Matchers.iterableWithSize(2));
        assertThat(news.get(0).getHeadline(), Matchers.equalTo("headline-1"));
    }

}
