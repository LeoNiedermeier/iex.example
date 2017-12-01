package io.github.leoniedermeier.iex.example.refdata;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;

public interface RefDataService {
    static final String CACHE_NAME_REF_DATA_SYMBOL = "RefDataSymbol";

    //  @Cacheable sollte eher in die Klasse statt im Interface. Aber wg. default Methode machen wir das hier.
    
    @Cacheable(cacheNames = CACHE_NAME_REF_DATA_SYMBOL, key = "'RefDataSymbols'")
    List<RefDataSymbol> getRefDataSymbols();

    @Cacheable(cacheNames = CACHE_NAME_REF_DATA_SYMBOL, key = "'StockSymbols'")
    default Collection<String> getStockSymbols() {
        return getRefDataSymbols().stream().map(RefDataSymbol::getSymbol).filter(StringUtils::isNotBlank)
                // convention: uppercase
                .map(String::toUpperCase).collect(Collectors.toSet());
    }

}
