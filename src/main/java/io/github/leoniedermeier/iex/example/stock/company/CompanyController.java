package io.github.leoniedermeier.iex.example.stock.company;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CompanyController {

	private final CompanyService companyService;

	public CompanyController(final CompanyService companyService) {
		this.companyService = companyService;
	}

	@RequestMapping("/stock/{symbol}/company")
	public String company(@PathVariable("symbol") final String symbol, final Model model) {
		final Company company = this.companyService.getCompany(symbol);
		model.addAttribute("company", company);
		return "stock/company";
	}
}
