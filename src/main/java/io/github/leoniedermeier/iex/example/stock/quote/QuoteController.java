package io.github.leoniedermeier.iex.example.stock.quote;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Controller
public class QuoteController {

    private final QuoteService quoteService;
    private final Scheduler scheduler;

    public QuoteController(final QuoteService quoteService, final Scheduler scheduler) {
        this.quoteService = quoteService;
        this.scheduler = scheduler;
    }

    @GetMapping("/stock/{symbol}/quote")
    ModelAndView quoteOverview(@PathVariable("symbol") final String symbol) {
        final ModelAndView modelAndView = new ModelAndView("stock/quote");

        // könnte man auch mit
        // java.util.concurrent.CompletableFuture.thenCombine(...) umsetzen

        // Die Security-Exceptions werden aufgefangen und weitergeworen.
        // Damit der Security-Context mitgegeben wird, wird der configurierte Scheduler
        // verwendet.
        // Die aufgerufenen Methoden von QuoteService liefern "normale" Objekte zurück.
        // Für ModelAndView braucht man dann die wirklichen Werte (man kann keine Mono darin platzieren)
        final Mono<Quote> currentQuote = //
                Mono.fromSupplier(() -> this.quoteService.currentQuote(symbol))
                        // die Ergebnisse gleich verarbeiten
                        .doOnNext(q -> modelAndView.addObject("current", q));

        final Mono<OpenClose> openClose = Mono.fromSupplier(() -> this.quoteService.openClose(symbol)).doOnNext(oc -> {
            modelAndView.addObject("open", oc.getOpen());
            modelAndView.addObject("close", oc.getClose());
        });

        // ruft die beiden Methoden parallel auf und wartet.
        // Ergebnis nicht beachtet, da die Teilergebnisse in doOnNext verarbeitet wurden
        Mono.zip(currentQuote.subscribeOn(this.scheduler), openClose.subscribeOn(this.scheduler)).block();

        return modelAndView;
    }

    // @GetMapping("/stock/{symbol}/quote")
    ModelAndView quoteOverview2(@PathVariable("symbol") final String symbol) {
        final ModelAndView modelAndView = new ModelAndView("stock/quote");

        new MonoWrapper()//
                .and(() -> this.quoteService.currentQuote(symbol), q -> modelAndView.addObject("current", q))
                .and(() -> this.quoteService.openClose(symbol), oc -> {
                    modelAndView.addObject("open", oc.getOpen());
                    modelAndView.addObject("close", oc.getClose());
                })//
                .subScribeOnAndBlock(this.scheduler);

        return modelAndView;
    }

    private static class MonoWrapper {

        private final List<Mono<?>> monos = new ArrayList<>();

        private MonoWrapper() {
        }

        <T> MonoWrapper and(final Supplier<T> supplier, final Consumer<? super T> consumer) {
            final Mono<?> mono = Mono.fromSupplier(supplier).doOnNext(consumer);
            this.monos.add(mono);
            return this;
        }

        void subScribeOnAndBlock(Scheduler scheduler) {
            monos.forEach(m -> m.subscribeOn(scheduler));
            Mono.zip(this.monos, x -> "x").block();
        }
    }
}
