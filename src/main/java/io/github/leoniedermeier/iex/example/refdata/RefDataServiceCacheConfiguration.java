package io.github.leoniedermeier.iex.example.refdata;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class RefDataServiceCacheConfiguration {

	@Bean
	public CaffeineCache refDataSymbolCache() {
		// https://docs.spring.io/spring-boot/docs/2.0.x-SNAPSHOT/reference/html/boot-features-caching.html#boot-features-caching-provider-generic
		// "Generic caching is used if the context defines at least one
		// org.springframework.cache.Cache bean. A CacheManager wrapping all beans of
		// that type is created."
		//
		// oder KLasse:
		// org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration
		// Also einfach Cache Beans erzeugen, der Rest geht von selbst.
		// Das ist nur notwendig, wenn verschiedene Konfigurationen für verschiedenen
		// Caches notwendig sind. Ansonsten könnte man das mit properties machen.
		final Cache<Object, Object> cache = Caffeine.newBuilder().expireAfterWrite(300, TimeUnit.MINUTES).maximumSize(2)
				.build();
		return new CaffeineCache(RefDataService.CACHE_NAME_REF_DATA_SYMBOL, cache);
	}
}
