package io.github.leoniedermeier.iex.example.stock.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import io.github.leoniedermeier.iex.example.refdata.RefDataService;

@Component
public class StockSymbolConstraintValidator implements ConstraintValidator<StockSymbolConstraint, String> {

    private RefDataService refDataService;

    public StockSymbolConstraintValidator(final RefDataService refDataService) {
        this.refDataService = refDataService;
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        // Kein Caching hier, da man es an dieser Stelle nicht steuern kann. Caching falls möglich übernimmt der Service.
        return this.refDataService.getStockSymbols().contains(StringUtils.upperCase(value));
    }

}