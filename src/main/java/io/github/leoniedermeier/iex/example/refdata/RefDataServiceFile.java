package io.github.leoniedermeier.iex.example.refdata;

import java.io.IOException;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Service
public class RefDataServiceFile implements RefDataService {

	private final ObjectMapper objectMapper;

	public RefDataServiceFile(final ObjectMapper objectMapper) {
		// autoconfigured Jackson ObjetMapper from JacksonAutoConfiguration
		this.objectMapper = objectMapper;
	}

	@Override
	@Cacheable(CACHE_NAME_REF_DATA_SYMBOL)
	public List<RefDataSymbol> getRefDataSymbols() {
		final ClassPathResource classPathResource = new ClassPathResource("refdata_symbols.json");
		try {
			final List<RefDataSymbol> readValue = this.objectMapper.readValue(classPathResource.getInputStream(),
					new TypeReference<List<RefDataSymbol>>() {
					});
			return readValue;
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
