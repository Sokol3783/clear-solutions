package com.example.clearsolutions.service;


import com.example.clearsolutions.model.User;
import com.example.clearsolutions.model.User.UserBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
public class UserService {

  private Set<User> users = new TreeSet(Comparator.comparingLong(User::getId));
  private static final AtomicLong sequence = new AtomicLong();
  private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

  public User save(@Validated  User user) {
     user.setId(sequence.incrementAndGet());
     users.add(user);
     return user;
  }

  @SneakyThrows
  public void update(User user){
    if (users.remove(user)) {
        users.add(user);
    } else {
      throw new UserServiceException(UserServiceException.USER_NOT_FOUND);
    }
  }


  @SneakyThrows
  public void updateRequiredFields(User user) {
    User saved = users.stream().filter(s -> s.getId() == user.getId()).findFirst()
        .orElseThrow(() -> new UserServiceException(UserServiceException.USER_NOT_FOUND));

    UserBuilder builderForUpdate = saved.toBuilder();

    updateNotNullFields(user, builderForUpdate);

    User updated = builderForUpdate.build();

    Set<ConstraintViolation<User>> validate = validator.validate(updated);
    if (validate.size() > 0){
      throw new ValidationException(validate.stream().map(s -> s.getMessage()).collect(Collectors.joining()));
    }

    users.remove(saved);
    users.add(updated);

  }

  private static void updateNotNullFields(User user, UserBuilder builderForUpdate) {
    if (user.getEmail() != null){
      builderForUpdate.email(user.getEmail());
    }

    if(user.getFirstName() != null){
      builderForUpdate.firstName(user.getFirstName());
    }

    if(user.getLastName() != null) {
      builderForUpdate.lastName(user.getLastName());
    }
    if(user.getAddress() != null) {
      builderForUpdate.address(user.getAddress());
    }

    if (user.getBirthDate() != null){
      builderForUpdate.birthDate(user.getBirthDate());
    }
  }

  @SneakyThrows
  public boolean delete(User user) {
    return users.remove(user);
  }

  @SneakyThrows
  public List<User> searchByDate(OffsetDateTime from , OffsetDateTime to){
    if (from.isAfter(to)) throw new UserServiceException("Date From should be less than date to!");
    return users.stream().filter(users -> users.getBirthDate().isAfter(from) && users.getBirthDate().isBefore(to)).toList();
  }

}
