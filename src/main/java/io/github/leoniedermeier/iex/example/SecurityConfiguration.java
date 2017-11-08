package io.github.leoniedermeier.iex.example;
import org.springframework.boot.actuate.autoconfigure.security.EndpointRequest;
import org.springframework.boot.autoconfigure.security.StaticResourceRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//	@Bean
//	public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//		return new InMemoryUserDetailsManager(
//				User.withDefaultPasswordEncoder().username("user").password("password")
//						.authorities("ROLE_USER").build(),
//				User.withDefaultPasswordEncoder().username("admin").password("admin")
//						.authorities("ROLE_ACTUATOR", "ROLE_USER").build());
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// @formatter:off
		http.authorizeRequests()
				.requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
//				.requestMatchers(EndpointRequest.to("status", "info")).permitAll()
//				.requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole("ACTUATOR")
				.requestMatchers(StaticResourceRequest.toCommonLocations()).permitAll()
				.antMatchers("/**").permitAll()
				//.antMatchers("/**").hasRole("USER")
				.and()
			.cors()
				.and()
			.httpBasic();
		// @formatter:on
	}

}