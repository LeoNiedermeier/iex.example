package io.github.leoniedermeier.iex.example.security;

import static io.github.leoniedermeier.iex.example.security.Roles.USER;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PreAuthorize;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@PreAuthorize("hasRole('" + USER + "')")
public @interface HasRoleUser {

}
