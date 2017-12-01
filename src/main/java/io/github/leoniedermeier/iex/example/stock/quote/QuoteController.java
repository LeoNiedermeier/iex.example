package io.github.leoniedermeier.iex.example.stock.quote;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import io.github.leoniedermeier.iex.example.stock.quote.QuoteDataCollector.QuoteOverview;
import io.github.leoniedermeier.iex.example.stock.validation.StockSymbolConstraint;

@Controller
@Validated
public class QuoteController {

    private final QuoteDataCollector quoteDataCollector;

    public QuoteController(final QuoteDataCollector quoteDataCollector) {
        this.quoteDataCollector = quoteDataCollector;
    }

    @GetMapping("/stock/{symbol}/quote")
    public ModelAndView quoteOverview(@StockSymbolConstraint @PathVariable("symbol") final String symbol) {
        final QuoteOverview quoteOverview = this.quoteDataCollector.quoteOverview(symbol);
        return new ModelAndView("stock/quote", "quoteOverview", quoteOverview);
    }

}
