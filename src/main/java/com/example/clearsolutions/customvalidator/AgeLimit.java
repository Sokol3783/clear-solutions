package com.example.clearsolutions.customvalidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeLimitValidator.class)
public @interface AgeLimit {
  int MINIMUM_AGE = 0;

  int minimumAge() default MINIMUM_AGE;
  String message() default "User too young for register!";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

}
