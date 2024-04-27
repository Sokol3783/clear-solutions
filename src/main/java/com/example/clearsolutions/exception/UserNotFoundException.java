package com.example.clearsolutions.exception;

public class UserNotFoundException extends RuntimeException {

  public static final String USER_NOT_FOUND = "User not found!";

  public UserNotFoundException() {
    super();
  }

  public UserNotFoundException(String message) {
    super(message);
  }
}
