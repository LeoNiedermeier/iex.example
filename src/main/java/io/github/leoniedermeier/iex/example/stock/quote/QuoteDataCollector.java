package io.github.leoniedermeier.iex.example.stock.quote;

import java.time.Duration;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.github.leoniedermeier.iex.example.security.HasRoleUser;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Service
public class QuoteDataCollector {

    public static class QuoteOverview {
        private Quote current;

        private Quote open;

        private Quote close;

        public QuoteOverview() {
            super();
        }

        public Quote getClose() {
            return this.close;
        }

        public Quote getCurrent() {
            return this.current;
        }

        public Quote getOpen() {
            return this.open;
        }

        public void setClose(final Quote close) {
            this.close = close;
        }

        public void setCurrent(final Quote current) {
            this.current = current;
        }

        public void setOpen(final Quote open) {
            this.open = open;
        }

    }

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(QuoteDataCollector.class);

    private static final <T> Mono<T> logAndReturnEmptyMono(final Throwable throwable) {
        LOGGER.error("Fehler bei Aufruf", throwable);
        return Mono.empty();
    }

    private final QuoteService quoteService;

    private final Scheduler scheduler;

    public QuoteDataCollector(final QuoteService quoteService, final Scheduler scheduler) {
        this.quoteService = quoteService;
        this.scheduler = scheduler;

    }

    @HasRoleUser
    public QuoteOverview quoteOverview(final String symbol) {

        // könnte man auch mit
        // java.util.concurrent.CompletableFuture.thenCombine(...) umsetzen

        // Die aufgerufenen Methoden von QuoteService liefern "normale" Objekte zurück.
        // Für ModelAndView braucht man dann die wirklichen Werte (man kann keine Mono
        // darin platzieren)
        QuoteOverview quoteOverview = new QuoteOverview();
        final Mono<Quote> currentQuote = //
                Mono.fromSupplier(() -> this.quoteService.currentQuote(symbol))
                        // nichts machen bei Fehler
                        .onErrorResume(QuoteDataCollector::logAndReturnEmptyMono)
                        // die Ergebnisse gleich verarbeiten
                        .doOnNext(q -> quoteOverview.setCurrent(q));

        final Mono<OpenClose> openClose = Mono.fromSupplier(() -> this.quoteService.openClose(symbol))
                // nichts machen bei Fehler
                .onErrorResume(QuoteDataCollector::logAndReturnEmptyMono)
                // Falls Daten kommen, diese einsortieren
                .doOnNext(oc -> {
                    quoteOverview.setOpen(oc.getOpen());
                    quoteOverview.setClose(oc.getClose());
                });

        // ruft die beiden Methoden parallel auf und wartet.
        // Ergebnis nicht beachtet, da die Teilergebnisse in doOnNext verarbeitet wurden
        Mono.zip(currentQuote.subscribeOn(this.scheduler), openClose.subscribeOn(this.scheduler))
                .block(Duration.ofSeconds(60));

        return quoteOverview;
    }
}
