package com.example.clearsolutions.controller;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toMap;

import com.example.clearsolutions.exception.InvalidDateException;
import com.example.clearsolutions.exception.UserNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandlingControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity notFound() {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity invalidFields(ConstraintViolationException exception) {
    Set<ConstraintViolation<?>> errors = exception.getConstraintViolations();
    Map<String, String> map = new HashMap<>();
    for (ConstraintViolation error : errors){
      map.merge(getEndOfPath(error.getPropertyPath()), error.getMessage(), (s1, s2) -> s1 + " " + s2);
    }

   return ResponseEntity.badRequest().body(map);
  }

  private String getEndOfPath(Path path) {
    String last = "";
    for (Path.Node node : path){
      last = node.toString();
    }
    return last;
  }

  @ExceptionHandler(InvalidDateException.class)
  public ResponseEntity invalidDatesInSearch(InvalidDateException ex){
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    BindingResult result = ex.getBindingResult();
    Map<String, String> groupedErrors = result.getFieldErrors().stream().collect(
      Collectors.groupingBy(FieldError::getField,
          mapping(FieldError::getDefaultMessage, joining())));
    return ResponseEntity.badRequest().body(groupedErrors);

  }

  @Override
  protected ResponseEntity<Object> handleHandlerMethodValidationException(
      HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status,
      WebRequest request) {
    return super.handleHandlerMethodValidationException(ex, headers, status, request);
  }
}
