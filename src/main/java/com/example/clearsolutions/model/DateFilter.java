package com.example.clearsolutions.model;

import java.time.OffsetDateTime;
import lombok.Data;

@Data
public class DateFilter {

  OffsetDateTime from;
  OffsetDateTime to;

}
