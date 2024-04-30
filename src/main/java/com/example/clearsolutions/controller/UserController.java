package com.example.clearsolutions.controller;

import com.example.clearsolutions.model.DateFilter;
import com.example.clearsolutions.model.User;
import com.example.clearsolutions.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class UserController {

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @PostMapping(path = "/users", produces = "application/json")
  public ResponseEntity<?> createUser(@Valid @RequestBody User user) {
    User save = service.save(user);
    return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(save);
  }

  @PatchMapping(path = "/users/{id}", produces = "application/json")
  public ResponseEntity<?> updateUserRequiredFields(@PathVariable long id, @RequestBody User user) {
    service.updateRequiredFields(id, user);
    return ResponseEntity.ok().build();
  }

  @PutMapping(path = "/users/{id}", produces = "application/json")
  public ResponseEntity<?> updateUser(@PathVariable long id, @Valid @RequestBody User user) {
    service.update(id, user);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping(path = "/users/{id}", produces = "application/json")
  public ResponseEntity<?> deleteUser(@PathVariable long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping(path = "/users/search", produces = "application/json")
  public ResponseEntity<?> searchUsersByDate(
      @Valid @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date 'FROM' should format in 'yyyy-mm-dd'") @RequestParam String from,
      @Valid @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date 'TO' should format in 'yyyy-mm-dd'") @RequestParam String to) {
    return ResponseEntity.ok().body(service.searchByDate(from, to));
  }

  @PostMapping(path = "/users/search", produces = "application/json")
  ResponseEntity<?> searchUsersByDate(@RequestBody @Valid DateFilter filter) {
    return ResponseEntity.ok().body(service.searchByDate(filter.getFrom(), filter.getTo()));
  }

}
