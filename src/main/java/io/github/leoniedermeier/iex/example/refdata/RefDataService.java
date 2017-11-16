package io.github.leoniedermeier.iex.example.refdata;

import java.util.List;

public interface RefDataService {
	static final String CACHE_NAME_REF_DATA_SYMBOL = "RefDataSymbol";

	public List<RefDataSymbol> getRefDataSymbols();
}
