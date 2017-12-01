package io.github.leoniedermeier.iex.example.stock.quote;

import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import io.github.leoniedermeier.iex.example.stock.quote.QuoteDataCollector.QuoteOverview;
import reactor.core.scheduler.Schedulers;

public class QuoteDataCollectorTest {

    @Test
    public void test() {
        final QuoteService quoteService = Mockito.mock(QuoteService.class);

        Mockito.when(quoteService.currentQuote("xyz")).thenReturn(new Quote(null, 100.12));

        final OpenClose openClose = new OpenClose();
        openClose.setOpen(new Quote(LocalDateTime.now().minusHours(2), 100.2d));
        openClose.setClose(new Quote(LocalDateTime.now(), 300d));

        Mockito.when(quoteService.openClose("xyz")).thenReturn(openClose);

        final QuoteDataCollector controller = new QuoteDataCollector(quoteService, Schedulers.immediate());
        final QuoteOverview overview = controller.quoteOverview("xyz");

        assertThat(overview.getCurrent().getPrice(), Matchers.closeTo(100.12d, 0d));

        assertThat(overview.getOpen().getPrice(), Matchers.closeTo(100.2d, 0d));

        assertThat(overview.getClose().getPrice(), Matchers.closeTo(300d, 0d));
    }

    @Test
    public void test_error() {
        final QuoteService quoteService = Mockito.mock(QuoteService.class);

        Mockito.when(quoteService.currentQuote("xyz")).thenReturn(new Quote(null, 100.12));

        Mockito.when(quoteService.openClose("xyz")).thenThrow(RuntimeException.class);
        // Mockito.when(quoteService.openClose("xyz")).thenAnswer(i -> {
        // throw new RuntimeException("EXCEPTION!");
        // });

        final QuoteDataCollector controller = new QuoteDataCollector(quoteService, Schedulers.immediate());

        // Log the exception and returns not null object
        QuoteOverview overview = controller.quoteOverview("xyz");

        assertThat(overview.getCurrent(), Matchers.notNullValue());
        assertThat(overview.getOpen(), Matchers.nullValue());
        assertThat(overview.getClose(), Matchers.nullValue());
    }

}
