package com.example.clearsolutions.model;

import com.example.clearsolutions.customvalidator.AgeLimit;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Exclude;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.springframework.beans.factory.annotation.Value;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
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
  @Exclude
  private String lastName;

  @AgeLimit
  @Exclude
  private OffsetDateTime birthDate;

  @Exclude
  private String address;

  @Exclude
  private String phone;

}
