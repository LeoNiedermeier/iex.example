package io.github.leoniedermeier.iex.example.stock.quote;

import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;

public class QuoteServiceTest {

    // @Test
    public void testCurrentQuote() {
        QuoteService quoteService = new QuoteService(new RestTemplateBuilder(),"https://api.iextrading.com/1.0");
        
        Object currentQuote = quoteService.currentQuote("aapl");
        System.out.println(currentQuote);

        OpenClose openClose = quoteService.openClose("aapl");
        System.out.println(openClose);
    }

    // https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html#webflux-client

  //  @Test
    public void testCurrentQuote2() {
        WebClient client = WebClient.builder().build();

        Mono<String> result = client.get().uri("https://api.iextrading.com/1.0/stock/aapl/price")
                .accept(MediaType.TEXT_PLAIN).retrieve().bodyToMono(String.class);
        System.out.println(result.block());
    }
    
    @Test
    public void testCurrentQuote3() {
        QuoteService quoteService = new QuoteService(new RestTemplateBuilder(),"https://api.iextrading.com/1.0");
        Mono<Quote> cq = Mono.fromSupplier(() -> quoteService.currentQuote("aapl") ).subscribeOn(Schedulers.parallel());
        Mono<OpenClose> oc = Mono.fromSupplier(() -> quoteService.openClose("aapl") ).subscribeOn(Schedulers.parallel());
        Tuple2<Quote, OpenClose> block = Mono.zip(cq, oc).block();
        System.out.println(block);
    }
}
