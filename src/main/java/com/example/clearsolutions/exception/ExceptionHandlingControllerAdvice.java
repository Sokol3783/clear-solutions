package com.example.clearsolutions.exception;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;

import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionHandlingControllerAdvice {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity notFound() {
    return ResponseEntity.notFound().build();

  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity invalidFields(BindingResult result) {
    Map<String, String> groupedErrors = result.getFieldErrors().stream().collect(
        Collectors.groupingBy(FieldError::getField,
            mapping(FieldError::getDefaultMessage, joining())));
    return ResponseEntity.badRequest().body(groupedErrors);
  }

  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(InvalidDateException.class)
  public ResponseEntity invalidDatesInSearch(InvalidDateException ex){
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

}
