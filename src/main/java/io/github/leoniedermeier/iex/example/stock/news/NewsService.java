package io.github.leoniedermeier.iex.example.stock.news;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ArrayUtils.nullToEmpty;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import io.github.leoniedermeier.iex.example.security.HasRoleUser;

@Service
public class NewsService {

    private final RestTemplate restTemplate;

    public NewsService(final RestTemplateBuilder builder, @Value("${iextrading.root.uri}") final String rootUri) {
        this.restTemplate = builder.rootUri(rootUri).build();
    }

    @HystrixCommand(
            // Fallback Methode muss die gleiche signatur haben
            fallbackMethod="getNewsFallback")
    @HasRoleUser
    // Die Security wird noch vor dem HystrixCommand ausgeführt. (wegen order)
    public List<News> getNews(final String symbol, final Integer range) {
        // /stock/{symbol}/news/last/{range}
        // symbol Use market to get market-wide news (i.e. .../market/news/...)
        // range Number between 1 and 50. Default is 10. (i.e. .../news/last/1)

        // Hinweis: bei Metrics wird die übergebene URL benutzt. Würde man den Parameter
        // schon vorher ersetzen, so hätte man für jeden Parameterwert eine andere URL
        // und damit einen weiteren Metrics
        // Eintrag.
        String url = "/stock/{symbol}/news";
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("symbol", symbol);
        if (range != null && 0 < range.intValue() && range.intValue() < 50) {
            url += "/last/{range}";
            parameters.put("range", range.toString());
        }

        final ResponseEntity<News[]> responseEntity = this.restTemplate.getForEntity(url, News[].class, parameters);

        return asList(nullToEmpty(responseEntity.getBody(), News[].class));
    }
    
    public List<News> getNewsFallback(final String symbol, final Integer range){
        News news = new News();
        news.setHeadline("No news available");
        return Collections.singletonList(news);
    }
}
