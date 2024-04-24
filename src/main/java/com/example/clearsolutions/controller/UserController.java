package com.example.clearsolutions.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("api")
public class UserController {

  @PostMapping
  ResponseEntity<?> createUser() {
    return ResponseEntity.badRequest().body(null);
  }


  @PatchMapping
  ResponseEntity<?> updateUserRequiredFields() {
    return null;
  }

  @PutMapping
  ResponseEntity<?> updateUser() {
    return null;
  }

  @DeleteMapping()
  ResponseEntity<?> deleteUser() {
    return null;
  }

  @GetMapping
  ResponseEntity<?> searchUsersByDate() {
    return null;
  }

}
