package com.example.clearsolutions.customvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.OffsetDateTime;

public class AgeLimitValidator implements ConstraintValidator<AgeLimit, OffsetDateTime> {

  int minimumAge;

  @Override
  public void initialize(AgeLimit constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(OffsetDateTime birthDate,
      ConstraintValidatorContext constraintValidatorContext) {

    if(birthDate == null){
      return false;
    }
    OffsetDateTime  today = OffsetDateTime.now();
    OffsetDateTime minimumAgeYearsAgo = today.minusYears(this.minimumAge);
    return !minimumAgeYearsAgo.isBefore(birthDate);

  }
}
