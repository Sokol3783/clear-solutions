package com.example.clearsolutions.customvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.OffsetDateTime;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Value;

public class AgeLimitValidator implements ConstraintValidator<AgeLimit, OffsetDateTime> {

  private int ageLimit;

  public AgeLimitValidator(@Value("${registration.age.limit}") int ageLimit) {
    this.ageLimit = ageLimit;
  }

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
    OffsetDateTime minimumAgeYearsAgo = today.minusYears(ageLimit);
    return !minimumAgeYearsAgo.isBefore(birthDate);

  }
}
