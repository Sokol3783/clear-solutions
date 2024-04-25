package com.example.clearsolutions.service;

public class UserServiceException extends Throwable {

  public static final String USER_NOT_FOUND = "User not found!";

  public UserServiceException() {
    super();
  }

  public UserServiceException(String message) {
    super(message);
  }
}
