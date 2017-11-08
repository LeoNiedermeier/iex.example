package io.github.leoniedermeier.iex.example.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class IexExampleHealthIndicator implements HealthIndicator {

	@Override
	public Health health() {
		
		return Health.up().build();
	}

}
