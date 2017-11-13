package io.github.leoniedermeier.iex.example.refdata;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RefDataController {

	private final RefDataService refDataService;

	public RefDataController(final RefDataService refDataService) {
		this.refDataService = refDataService;
	}

	@RequestMapping("/refdata")
	public ModelAndView refdata(@RequestParam(name = "fromIndex", required = false, defaultValue = "0") int fromIndex,
			@RequestParam(name = "length", required = false, defaultValue = "15") int length) {
		final ModelAndView model = new ModelAndView("refdata");
		final List<RefDataSymbol> refDataSymbols = this.refDataService.getRefDataSymbols();

		if (length < 0) {
			length = 15;
		}
		if (fromIndex < 0) {
			fromIndex = 0;
		}
		if (fromIndex > refDataSymbols.size()) {
			fromIndex = refDataSymbols.size() - 1;
		}

		List<RefDataSymbol> subList = refDataSymbols.subList(fromIndex, fromIndex + 15);
		model.addObject("refDataSymbols", subList);
		return model;
	}
}
