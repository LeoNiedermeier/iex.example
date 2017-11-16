package io.github.leoniedermeier.iex.example.refdata;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RefDataController {

	public static class CharAndIndex {
		private final Character character;

		private final int index;

		public CharAndIndex(final Character character, final int index) {
			super();
			this.index = index;
			this.character = character;
		}

		public Character getCharacter() {
			return this.character;
		}

		public int getIndex() {
			return this.index;
		}

	}

	public static class SearchFields {
		private String name;
		private String symbol;

		private int fromIndex;

		public int getFromIndex() {
			return this.fromIndex;
		}

		public String getName() {
			return this.name;
		}

		public String getSymbol() {
			return this.symbol;
		}

		public void setFromIndex(final int fromIndex) {
			this.fromIndex = fromIndex;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public void setSymbol(final String symbol) {
			this.symbol = symbol;
		}
	}

	private final RefDataService refDataService;

	private final int length = 15;

	public RefDataController(final RefDataService refDataService) {
		this.refDataService = refDataService;
	}

	private int checkAndAdjustRange(final int index, final int minValue, final int maxValue) {
		if (index < minValue) {
			return minValue;
		} else if (index > maxValue) {
			return maxValue;
		}
		return index;
	}

	private List<RefDataSymbol> filterList(final List<RefDataSymbol> refDataSymbols, final SearchFields searchFields) {
		// Predicate für Suche mit optinalen Elementen zusammenbauen: geht mit and(...)
		Predicate<RefDataSymbol> predicate = r -> true;
		if (isNoneEmpty(searchFields.getName())) {
			predicate = predicate.and(p -> containsIgnoreCase(p.getName(), searchFields.getName()));
		}

		if (isNoneEmpty(searchFields.getSymbol())) {
			predicate = predicate.and(p -> containsIgnoreCase(p.getSymbol(), searchFields.getSymbol()));
		}
		final List<RefDataSymbol> filtered = refDataSymbols.stream().filter(predicate).collect(Collectors.toList());
		return filtered;
	}

	private List<RefDataSymbol> getRefDataSymbols() {
		// 1. prepare list: filter null and sort
		final List<RefDataSymbol> refDataSymbols = this.refDataService.getRefDataSymbols();

		final List<RefDataSymbol> sortedList = refDataSymbols.stream().filter(p -> isNotEmpty(p.getName()))
				// ignore-case vergleich, daher geht
				// Comparator.comparing(RefDataSymbol::getName) nicht
				.sorted((r1, r2) -> StringUtils.compareIgnoreCase(r1.getName(), r2.getName()))
				.collect(Collectors.toList());
		return sortedList;
	}

	private void prepareCharToIndex(final ModelAndView model, final List<RefDataSymbol> sortedList) {
		// Den counter extra zu machen ist nicht so schön
		// Hinweis: ein einfaches int ist nicht effective final.
		final AtomicInteger counter = new AtomicInteger(-1);
		final List<CharAndIndex> charToIndex = sortedList.stream()
				.map(s -> Character.toUpperCase(s.getName().charAt(0))).peek(c -> counter.incrementAndGet())
				// nur die verschiedenen chars, es wird das erste zurückgegeben (javadoc)
				.distinct().map(c -> new CharAndIndex(c, counter.get())).collect(Collectors.toList());

		model.addObject("charToIndex", charToIndex);
	}

	private void prepareTableWithPaging(final int fromIndex, final ModelAndView model,
			final List<RefDataSymbol> sortedList) {
		// prepare paging
		final int startIndex = checkAndAdjustRange(fromIndex, 0, sortedList.size());

		final int endIndex = checkAndAdjustRange(startIndex + this.length, startIndex, sortedList.size());

		final List<RefDataSymbol> subList = sortedList.subList(startIndex, endIndex);
		model.addObject("refDataSymbols", subList);

		if (startIndex > 0) {
			// index darf nicht kleiner als 0 werden, auch wenn startIndex-length < 0
			model.addObject("previousPagingIndex", Math.max(0, startIndex - this.length));
		}

		if (endIndex < sortedList.size()) {
			model.addObject("nextPagingIndex", endIndex);
		}
	}

	@GetMapping("/refdata")
	public ModelAndView refdata(
			@RequestParam(name = "fromIndex", required = false, defaultValue = "0") final int fromIndex) {
		final ModelAndView model = new ModelAndView("refdata/refdata");

		final List<RefDataSymbol> refDataSymbols = getRefDataSymbols();

		prepareCharToIndex(model, refDataSymbols);
		prepareTableWithPaging(fromIndex, model, refDataSymbols);

		model.addObject("searchFields", new SearchFields());
		return model;
	}

	@PostMapping(value = "/refdata", params = { "reset" })
	public ModelAndView reset() {
		return this.refdata(0);
	}

	@PostMapping("/refdata")
	public ModelAndView search(@ModelAttribute final SearchFields searchFields) {

		final ModelAndView model = new ModelAndView("refdata/refdata");

		final List<RefDataSymbol> refDataSymbols = getRefDataSymbols();

		final List<RefDataSymbol> filtered = filterList(refDataSymbols, searchFields);
		prepareCharToIndex(model, filtered);
		prepareTableWithPaging(searchFields.getFromIndex(), model, filtered);

		model.addObject("searchFields", searchFields);
		return model;
	}
}
