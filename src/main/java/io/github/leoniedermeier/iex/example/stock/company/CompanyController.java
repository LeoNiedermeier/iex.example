package io.github.leoniedermeier.iex.example.stock.company;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CompanyController {

	private final CompanyService companyService;

	public CompanyController(final CompanyService companyService) {
		this.companyService = companyService;
	}

	@RequestMapping("/stock/company/{symbol}")
	public String company(@PathVariable("symbol") String symbol, Model model) {
		Company company = this.companyService.getCompany(symbol);
		System.out.println(company);
		model.addAttribute("company", company);
		return "stock/company";
	}
}
