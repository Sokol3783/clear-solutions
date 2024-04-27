package com.example.clearsolutions.model;

import com.example.clearsolutions.customvalidator.AgeLimit;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Exclude;
import org.springframework.beans.factory.annotation.Value;

@Data
@EqualsAndHashCode
@Builder(toBuilder = true)
public class User {
  @Value("${registration.age.limit}")
  private static final int MINIMUM_AGE = 0;
  private long id;

  @Email
  @Exclude
  private String email;

  @NotBlank
  @Exclude
  private String firstName;

  @NotBlank
  private String lastName;

  @AgeLimit(minimumAge = MINIMUM_AGE, message = "User must be at least" + MINIMUM_AGE + " years old")
  private OffsetDateTime birthDate;

  @Exclude
  private String address;

  @Exclude
  private String phone;

}
