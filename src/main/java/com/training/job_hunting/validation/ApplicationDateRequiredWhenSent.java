package com.training.job_hunting.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ApplicationDateRequiredValidator.class)
public @interface ApplicationDateRequiredWhenSent {
    String message() default "Application date is required once the application has been sent.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
