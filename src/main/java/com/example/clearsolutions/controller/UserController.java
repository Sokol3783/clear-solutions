package com.example.clearsolutions.controller;

import com.example.clearsolutions.model.DateFilter;
import com.example.clearsolutions.model.User;
import com.example.clearsolutions.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("users")
public class UserController {

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping(produces = "application/json")
  ResponseEntity<?> createUser(@Valid @RequestBody User user) {
    return ResponseEntity.badRequest().body(null);
  }


  @PatchMapping(path = "/{id}", produces = "application/json")
  ResponseEntity<?> updateUserRequiredFields(@PathVariable long id, @RequestBody User user) {
    return null;
  }

  @PutMapping(path = "/{id}", produces = "application/json")
  ResponseEntity<?> updateUser(@Valid @PathVariable long id, @RequestBody User user) {
    return null;
  }

  @DeleteMapping(path = "/{id}", produces = "application/json")
  ResponseEntity<?> deleteUser(@PathVariable long id) {
    return null;
  }

  @GetMapping(path = "/search", produces = "application/json")
  ResponseEntity<?> searchUsersByDate(@RequestParam String from, @RequestParam String to) {
    return null;
  }

  @PostMapping(path = "/search", produces = "application/json")
  ResponseEntity<?> searchUsersByDate(@RequestBody DateFilter filter) {
    return null;
  }

}
