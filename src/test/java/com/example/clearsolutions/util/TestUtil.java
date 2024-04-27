package com.example.clearsolutions.util;

import com.example.clearsolutions.model.User;
import com.example.clearsolutions.service.UserService;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

public class TestUtil {

  public static void addTestUsers(UserService service) {
    List<User> testUsers = getTestUsers();
    for (User user : testUsers) {
      service.save(user);
    }
  }

  public static List<User> getTestUsers() {
    return List.of(
        User.builder().email("example@gmail.com").phone("phone").firstName("first_name1")
            .lastName("last_name")
            .birthDate(new OffsetBuilder(1993).build()).build(),
        User.builder().email("example@gmail.com").phone("phone").firstName("first_name1")
            .lastName("last_name2")
            .birthDate(new OffsetBuilder(1958).build()).build(),
        User.builder().email("example@gmail.com").phone("phone").firstName("first_name1")
            .lastName("last_name3")
            .birthDate(new OffsetBuilder(2000).build()).build());

  }

  public static User getTestUser() {
    return User.builder().email("example@gmail.com").phone("phone").firstName("first_name1")
        .lastName("last_name")
        .birthDate(new OffsetBuilder(1993).build()).build();
  }

  public static class OffsetBuilder {

    private int year;

    public OffsetBuilder(int year) {
      this.year = year;
    }

    public LocalDateTime build() {
      ZoneOffset zone = ZoneOffset.UTC;
      return LocalDateTime.of(year, 12, 5, 0, 0, 0);
    }
  }

}
