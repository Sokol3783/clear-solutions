package com.example.clearsolutions.service;


import com.example.clearsolutions.exception.InvalidDateException;
import com.example.clearsolutions.exception.UserNotFoundException;
import com.example.clearsolutions.model.User;
import com.example.clearsolutions.model.User.UserBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final AtomicLong sequence = new AtomicLong();

  private Validator validator;

  private final Set<User> users = new TreeSet(Comparator.comparingLong(User::getId));

  public UserService(@Autowired Validator validator) {
    this.validator = validator;
  }

  public User save(User user) {
    User build = user.toBuilder().id(sequence.incrementAndGet()).build();
    users.add(build);
    return build;
  }

  @SneakyThrows
  public void update(long id, User user) {
    User old = findById(id);
    users.remove(old);
    users.add(user);
  }

  @SneakyThrows
  public void updateRequiredFields(long id, User userForUpdate) {
    User saved = findById(id);
    User updated = updateNotNullFields(userForUpdate, saved.toBuilder());

    Set<ConstraintViolation<User>> validate = validator.validate(updated);
    if (!validate.isEmpty()) {
      throw new ConstraintViolationException(validate);
    }

    users.remove(saved);
    users.add(updated);

  }

  private User updateNotNullFields(User user, UserBuilder builderForUpdate) {
    if (user.getEmail() != null) {
      builderForUpdate.email(user.getEmail());
    }

    if (user.getFirstName() != null) {
      builderForUpdate.firstName(user.getFirstName());
    }

    if (user.getLastName() != null) {
      builderForUpdate.lastName(user.getLastName());
    }
    if (user.getAddress() != null) {
      builderForUpdate.address(user.getAddress());
    }

    if (user.getBirthDate() != null) {
      builderForUpdate.birthDate(user.getBirthDate());
    }
    return builderForUpdate.build();
  }

  @SneakyThrows
  public void delete(long id) {
    users.remove(findById(id));
  }

  @SneakyThrows
  public List<User> searchByDate(String from, String to) {

    OffsetDateTime fromDate = OffsetDateTime.parse(from + "T00:00:00+00"); // (from);
    OffsetDateTime toDate = OffsetDateTime.parse(to + "T00:00:00+00"); // (from);

    if (fromDate.isAfter(toDate)) {
      throw new InvalidDateException("Date 'From' should be less than date 'To'!");
    }

    return users.stream().filter(
            user -> inRangeOfDates(user, fromDate, toDate))
        .toList();

  }

  private static boolean inRangeOfDates(User user, OffsetDateTime fromDate, OffsetDateTime toDate) {
    return user.getBirthDate().isAfter(fromDate) && user.getBirthDate().isBefore(toDate);
  }

  public User findById(long id) throws UserNotFoundException {
    return users.stream().filter(s -> s.getId() == id).findFirst()
        .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));
  }

}
