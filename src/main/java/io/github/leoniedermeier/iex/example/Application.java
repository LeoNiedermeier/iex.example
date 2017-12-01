package io.github.leoniedermeier.iex.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.core.Ordered;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication

// alle @EnabledXyz zentral hier, Details eventuell in den Configurer Klassen
@EnableGlobalMethodSecurity(prePostEnabled = true,
        // security Interceptor als erstes ausführen, auf alle Fälle vor Caching.
        order = Ordered.HIGHEST_PRECEDENCE)

@EnableCaching(order = 0)
// hat per default die LOWEST_PRECEDENCE
@EnableCircuitBreaker
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
