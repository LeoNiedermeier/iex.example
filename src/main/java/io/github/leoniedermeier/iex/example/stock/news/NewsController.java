package io.github.leoniedermeier.iex.example.stock.news;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import io.github.leoniedermeier.iex.example.stock.validation.StockSymbolConstraint;

@Controller
@Validated
public class NewsController {

	private final NewsService newsService;

	public NewsController(final NewsService newsService) {
		this.newsService = newsService;
	}

	@GetMapping({ "/stock/{symbol}/news", "/stock/{symbol}/news/last/{range}" })
	public ModelAndView news(@StockSymbolConstraint @PathVariable("symbol") final String symbol,
			@PathVariable(name = "range", required = false) final Integer range) {
		final List<News> news = this.newsService.getNews(symbol, range);
		return new ModelAndView("stock/news", "news", news);
	}
}
