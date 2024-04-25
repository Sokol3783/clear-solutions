package com.example.clearsolutions.controller;

import com.example.clearsolutions.model.DateFilter;
import com.example.clearsolutions.model.User;
import com.example.clearsolutions.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatusCode;
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
  public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
    return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(user);
  }

  @PatchMapping(path = "/{id}", produces = "application/json")
  public ResponseEntity updateUserRequiredFields(@PathVariable long id, @RequestBody User user) {
    service.updateRequiredFields(id, user);
    return ResponseEntity.ok().build();
  }

  @PutMapping(path = "/{id}", produces = "application/json")
  public ResponseEntity<?> updateUser(@Valid @PathVariable long id, @RequestBody User user) {
    service.update(id, user);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(path = "/{id}", produces = "application/json")
  public ResponseEntity<?> deleteUser(@PathVariable long id) {
    if (service.delete(id)) {
      return ResponseEntity.noContent().build();
    }

    return ResponseEntity.notFound().build();
  }

  @GetMapping(path = "/search", produces = "application/json")
  public ResponseEntity<?> searchUsersByDate(
      @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date should format in 'yyyy-mm-dd'") @RequestParam String from,
      @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date should format in 'yyyy-mm-dd'") @RequestParam String to) {
    return ResponseEntity.ok().body(service.searchByDate(from, to));
  }

  @PostMapping(path = "/search", produces = "application/json")
  ResponseEntity<?> searchUsersByDate(@RequestBody DateFilter filter) {
    return ResponseEntity.ok().body(service.searchByDate(filter.getFrom(), filter.getTo()));
  }

}
