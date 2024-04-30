package com.example.clearsolutions.controller;


import static com.example.clearsolutions.util.TestUtil.getTestUser;
import static com.example.clearsolutions.util.TestUtil.getTestUsers;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.clearsolutions.exception.UserNotFoundException;
import com.example.clearsolutions.model.DateFilter;
import com.example.clearsolutions.model.User;
import com.example.clearsolutions.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

  private final static String USERS_1_TEMPLATE = "/users/1";
  private final static String SEARCH_TEMPLATE = "/users/search";

  @Autowired
  Validator validator;
  @Autowired
  ObjectMapper mapper;
  @Autowired
  MockMvc mvc;
  @MockBean
  UserService service;

  @Test
  void shouldReturnUserNotFoundForNonExistsOne() throws JsonProcessingException {
    User user = getTestUser();

    doThrow(UserNotFoundException.class).when(service).delete(1l);
    doThrow(UserNotFoundException.class).when(service).update(1l, user);
    doThrow(UserNotFoundException.class).when(service).updateRequiredFields(1l, user);

    String body = mapper.writeValueAsString(user);

    assertAll(() -> mvc.perform(delete(USERS_1_TEMPLATE)).andExpect(status().isNotFound()),
        () -> mvc.perform(
                put(USERS_1_TEMPLATE).content(body).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound()), () -> mvc.perform(
                patch(USERS_1_TEMPLATE).content(body).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound()));
  }

  @Test
  void shouldReturnNoContentThanNotFound() throws Exception {
    doNothing().doThrow(new UserNotFoundException()).when(service).delete(1l);
    assertAll(() -> mvc.perform(delete(USERS_1_TEMPLATE)).andExpect(status().isNoContent()),
        () -> mvc.perform(delete(USERS_1_TEMPLATE)).andExpect(status().isNotFound()));

  }

  @Test
  void shouldReturnMessageWithMessageForInvalidFields() throws Exception {
    User user = User.builder().email(" ").lastName(" ").build();
    String emptyUser = mapper.writeValueAsString(user);
    Set<ConstraintViolation<User>> validate = validator.validate(user);
    doThrow(new ConstraintViolationException(validate)).when(service)
        .updateRequiredFields(anyLong(), any());

    assertAll(() -> mvc.perform(
                put(USERS_1_TEMPLATE).contentType(MediaType.APPLICATION_JSON).content(emptyUser))
            .andExpect(status().isBadRequest()),
        () -> mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(emptyUser))
            .andExpect(status().isBadRequest()), () -> mvc.perform(
            patch(USERS_1_TEMPLATE).contentType(MediaType.APPLICATION_JSON).content(emptyUser)));
  }

  @Test
  void shouldReturnMessageWithInvalidDataFormatForSearchWhenGetMethod() throws Exception {
    DateFilter filter = new DateFilter("1238712983712", "12376127312");

    mvc.perform(
            get(SEARCH_TEMPLATE).queryParam("from", filter.getFrom()).queryParam("to", filter.getTo()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.from").value("Date 'FROM' should format in 'yyyy-mm-dd'"))
        .andExpect(jsonPath("$.to").value("Date 'TO' should format in 'yyyy-mm-dd'"));
  }

  @Test
  void shouldReturnMessageWithInvalidDataFormatForSearchWhenPostMethod() throws Exception {
    DateFilter filter = new DateFilter("1238712983712", "12376127312");

    mvc.perform(post(SEARCH_TEMPLATE).content(mapper.writeValueAsString(filter))
            .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.from").value("Date 'FROM' should format in 'yyyy-mm-dd'"))
        .andExpect(jsonPath("$.to").value("Date 'TO' should format in 'yyyy-mm-dd'"));
  }

  @Test
  void shouldReturnArrayWithThreeUsers() throws JsonProcessingException {
    List<User> testUsers = getTestUsers();
    when(service.searchByDate(any(), any())).thenReturn(testUsers);
    DateFilter filter = new DateFilter("1900-01-01", "2025-01-01");
    String expectedValue = mapper.writeValueAsString(testUsers);
    assertAll(() -> mvc.perform(
            get(SEARCH_TEMPLATE).queryParam("from", filter.getFrom()).queryParam("to", filter.getTo()))
        .andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
        .andExpect(content().string(expectedValue)), () -> mvc.perform(
            post(SEARCH_TEMPLATE).content(mapper.writeValueAsString(filter))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray()).andExpect(content().string(expectedValue)));

  }


}