package com.example.clearsolutions.model;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class DateFilter {

  @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date should format in 'yyyy-mm-dd'")
  String from;
  @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date should format in 'yyyy-mm-dd'")
  String to;

}
