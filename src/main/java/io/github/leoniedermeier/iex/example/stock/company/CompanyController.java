package io.github.leoniedermeier.iex.example.stock.company;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import io.github.leoniedermeier.iex.example.security.HasRoleAdmin;
import io.github.leoniedermeier.iex.example.stock.validation.StockSymbolConstraint;

@Controller
public class CompanyController {

	private final CompanyService companyService;

	public CompanyController(final CompanyService companyService) {
		this.companyService = companyService;
	}

	@GetMapping("/stock/{symbol}/company")
	@HasRoleAdmin
	public ModelAndView company(@StockSymbolConstraint @PathVariable("symbol") final String symbol) {
		ModelAndView model = new ModelAndView("stock/company");
		final Company company = this.companyService.getCompany(symbol);
		model.addObject("company", company);
		return model;
	}
}
