package com.example.clearsolutions.customvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class AgeLimitValidator implements ConstraintValidator<AgeLimit, LocalDateTime> {


  @Override
  public void initialize(AgeLimit constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(LocalDateTime birthDate,
      ConstraintValidatorContext constraintValidatorContext) {

    if(birthDate == null){
      return false;
    }
    LocalDateTime  today = LocalDateTime.now();
    LocalDateTime minimumAgeYearsAgo = today.minusYears(AgeLimit.MINIMUM_AGE);
    return !minimumAgeYearsAgo.isBefore(birthDate);

  }
}
