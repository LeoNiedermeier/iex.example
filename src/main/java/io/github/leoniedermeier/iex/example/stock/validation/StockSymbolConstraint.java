package io.github.leoniedermeier.iex.example.stock.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StockSymbolConstraintValidator.class)
// https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/?v=6.0#validator-customconstraints-simple
public @interface StockSymbolConstraint {
    
    // message interpolation: see https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/?v=6.0#chapter-message-interpolation
    String message() default "The symbol ${validatedValue} is not known.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}