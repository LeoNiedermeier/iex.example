package io.github.leoniedermeier.iex.example.refdata;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RefDataController {

	private final RefDataService refDataService;

	public RefDataController(final RefDataService refDataService) {
		this.refDataService = refDataService;
	}

	@RequestMapping("/refdata")
	public String refdata(final Model model) {
		final List<RefDataSymbol> refDataSymbols = this.refDataService.getRefDataSymbols();
		model.addAttribute("refDataSymbols", refDataSymbols);
		return "refdata";
	}
}
