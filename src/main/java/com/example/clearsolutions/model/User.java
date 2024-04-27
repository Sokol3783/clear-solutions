package com.example.clearsolutions.model;

import com.example.clearsolutions.customvalidator.AgeLimit;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Exclude;

@Data
@EqualsAndHashCode
@Builder(toBuilder = true)
public class User {

   private long id;

  @Email
  @Exclude
  private String email;

  @NotBlank(message = "First name should not be empty!")
  @Exclude
  private String firstName;

  @NotBlank(message = "Last name should not be empty!")
  private String lastName;

  @AgeLimit
  private LocalDateTime birthDate;

  @Exclude
  private String address;

  @Exclude
  private String phone;

}
