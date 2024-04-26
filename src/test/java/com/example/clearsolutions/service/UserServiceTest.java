package com.example.clearsolutions.service;

import static com.example.clearsolutions.util.TestUtil.addTestUsers;
import static com.example.clearsolutions.util.TestUtil.getTestUser;
import static com.example.clearsolutions.util.TestUtil.getTestUsers;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.clearsolutions.exception.UserServiceException;
import com.example.clearsolutions.model.User;
import jakarta.validation.ValidationException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
class UserServiceTest {

  @Autowired
  private UserService service;

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void shouldThrowUserNotFoundExceptionWhenThereAreNoUser() {
    addTestUsers(service);
    User user = getTestUser();
    assertAll(() -> assertThrows(UserServiceException.class, () -> service.delete(5L)),
        () -> assertThrows(UserServiceException.class, () -> service.update(5L, user)),
        () -> assertThrows(UserServiceException.class, () -> service.updateRequiredFields(5L, user))
    );

  }

  @Test
  void shouldThrowInvalidDateFilter() {
    UserServiceException userServiceException = assertThrows(UserServiceException.class,
        () -> service.searchByDate("1995-12-13", "1994-12-13"));
    assertTrue(userServiceException.getMessage()
        .contentEquals("Date 'From' should be less than date 'To'!"));
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void shouldCreateUser() {
    addTestUsers(service);
    User user = getTestUser();
    User save = service.save(user);
    assertAll(() -> assertNotEquals(user, save),
              () -> assertEquals(4, save.getId())
    );
  }

  @Test
  void shouldThrowValidateExceptionWhenUpdateRequiredFieldsNotValid() {
    addTestUsers(service);
    User emptyUser = User.builder().build();
    assertThrows(ValidationException.class, () -> service.updateRequiredFields(1L, emptyUser));
  }

  @Test
  void shouldReturnEmptyListBecauseDateOf() {
    addTestUsers(service);
    List<User> users = service.searchByDate("1900-02-01", "1950-02-01");
    assertTrue(users.isEmpty());
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void shouldReturnAllDatesFromSearch() {
    addTestUsers(service);
    List<User> users = service.searchByDate("1900-02-01", "2022-02-01");
    assertAll(() -> assertFalse(users.isEmpty()),
        () -> assertEquals(3, users.size())
    );
  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void shouldReturnTwoDatesFrom1990To1999() {
    List<User> testUsers = getTestUsers();
    User user_1993 = testUsers.get(0);
    User user_1950 = testUsers.get(1);

    addTestUsers(service);
    List<User> users = service.searchByDate("1900-02-01", "1999-02-01");
    assertAll(() -> assertFalse(users.isEmpty()),
        () -> assertEquals(2, users.size()),
        () -> assertTrue(
            users.stream().anyMatch(s -> s.getBirthDate().isEqual(user_1993.getBirthDate()))),
        () -> assertTrue(
            users.stream().anyMatch(s -> s.getBirthDate().isEqual(user_1950.getBirthDate())))
    );

  }

  @Test
  @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
  void shouldReturnTwoDatesFrom1990To2000() {
    List<User> testUsers = getTestUsers();
    User user_1993 = testUsers.get(0);
    User user_2000 = testUsers.get(2);
    addTestUsers(service);

    List<User> users = service.searchByDate("1990-02-01", "2003-02-01");
    assertAll(() -> assertFalse(users.isEmpty()),
        () -> assertEquals(2, users.size()),
        () -> assertTrue(
            users.stream().anyMatch(s -> s.getBirthDate().isEqual(user_2000.getBirthDate()))),
        () -> assertTrue(
            users.stream().anyMatch(s -> s.getBirthDate().isEqual(user_1993.getBirthDate())))
    );

  }

}