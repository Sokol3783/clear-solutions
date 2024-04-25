package com.example.clearsolutions.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.OffsetDateTime;
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

  @NotBlank
  @Exclude
  private String firstName;

  @NotBlank
  private String lastName;

  @Past
  @Exclude
  private OffsetDateTime birthDate;

  @Exclude
  private String address;

  @Exclude
  private String phone;

}
