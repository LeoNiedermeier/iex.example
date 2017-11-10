package io.github.leoniedermeier.iex.example.stock.company;

import static java.util.concurrent.TimeUnit.MINUTES;

import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class CompanyServiceCacheConfiguration {

	@Bean
	public CaffeineCache companyCache() {
		return new CaffeineCache(CompanyService.CACHE_NAME_COMPANY,
				Caffeine.newBuilder().expireAfterWrite(30, MINUTES).maximumSize(100).build());
	}
}
