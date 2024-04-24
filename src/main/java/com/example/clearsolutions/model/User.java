package com.example.clearsolutions.model;

import jakarta.annotation.Nonnull;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

  private long id;
  @Nonnull
  private String email;
  @Nonnull
  private String firstName;
  @Nonnull
  private String LastName;
  @Nonnull
  private OffsetDateTime birthDate;
  private String address;
  private String phone;

}
