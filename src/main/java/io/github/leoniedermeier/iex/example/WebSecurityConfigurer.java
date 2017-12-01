package io.github.leoniedermeier.iex.example;

import static io.github.leoniedermeier.iex.example.security.Roles.ADMIN;
import static io.github.leoniedermeier.iex.example.security.Roles.USER;

import org.springframework.boot.actuate.autoconfigure.security.EndpointRequest;
import org.springframework.boot.autoconfigure.security.StaticResourceRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        final InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user").password("{noop}user").roles(USER).build());
        manager.createUser(User.withUsername("admin").password("{noop}admin").roles(USER, ADMIN).build());
        return manager;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        // @formatter:off
		http.authorizeRequests()
		        // actuator
		        .requestMatchers(EndpointRequest.toAnyEndpoint()).hasRole(ADMIN)

		        // static resources
		        .requestMatchers(StaticResourceRequest.toCommonLocations()).permitAll()
		        // news nur für eingeloggte user:
		        .mvcMatchers("/stock/*/news/**").hasRole(USER)
		        // company details nur für admin
		        .mvcMatchers("/stock/*/company/**").hasRole(ADMIN)
		        .mvcMatchers("/stock/*/quote").permitAll()
		        .mvcMatchers("/refdata").permitAll()
		        .mvcMatchers("/**").denyAll()
		   .and()
			    // url für Page angeben, sonst kommt default Page, 
			    // natürlich für alle permit, sonst kann man sich nicht einloggen
			    .formLogin().loginPage("/login").permitAll()
			.and()
			    // nicht nach /login?success gehen, sondern nach: / - logoutSuccessUrl konfigurieren
			    .logout().logoutSuccessUrl("/").permitAll();
			   // .httpBasic();
		// @formatter:on
    }

}