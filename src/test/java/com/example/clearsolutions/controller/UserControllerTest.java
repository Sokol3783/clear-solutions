package com.example.clearsolutions.controller;


import static com.example.clearsolutions.util.TestUtil.getTestUsers;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.clearsolutions.exception.UserNotFoundException;
import com.example.clearsolutions.model.DateFilter;
import com.example.clearsolutions.model.User;
import com.example.clearsolutions.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
  MockMvc mvc;

  @MockBean
  UserService service;

  @Test
  void shouldReturnUserNotFoundForNonExistsOne() {
    doThrow(UserNotFoundException.class).when(service).delete(any(Long.class));
    doThrow(UserNotFoundException.class).when(service).update(any(Long.class), any(User.class));
    doThrow(UserNotFoundException.class).when(service)
        .updateRequiredFields(any(Long.class), any(User.class));

    assertAll(() -> mvc.perform(delete(USERS_1_TEMPLATE)).andExpect(status().isNotFound()),
        () -> mvc.perform(put(USERS_1_TEMPLATE)).andExpect(status().isNotFound()),
        () -> mvc.perform(patch(USERS_1_TEMPLATE)).andExpect(status().isNotFound()));
  }

  @Test
  void shouldReturnNoContentThanNotFound() {
    doNothing().when(service).delete(any(Long.class));
    doThrow().when(service).delete(any(Long.class));

    assertAll(() -> mvc.perform(delete(USERS_1_TEMPLATE)).andExpect(status().isNoContent()),
        () -> mvc.perform(delete(USERS_1_TEMPLATE)).andExpect(status().isNotFound()));

  }

  @Test
  void shouldReturnMessageWithMessageForInvalidFields() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String emptyUser = mapper.writeValueAsString(User.builder().build());

    assertAll(
        () -> mvc.perform(
                put(USERS_1_TEMPLATE).contentType(MediaType.APPLICATION_JSON).content(emptyUser))
            .andExpect(status().isBadRequest()),
        () -> mvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON).content(emptyUser))
            .andExpect(status().isBadRequest()),
        () -> mvc.perform(
                patch(USERS_1_TEMPLATE).contentType(MediaType.APPLICATION_JSON).content(emptyUser))
            .andExpect(status().isBadRequest())
    );
  }

  @Test
  void shouldReturnMessageWithInvalidDataFormatForSearch() {

    assertAll(() -> mvc.perform(get(SEARCH_TEMPLATE + "?from=1238712983712&to=123761`27312"))
            .andExpect(status().isBadRequest()).andExpect(
                jsonPath("$.from").value("Date 'FROM' should format in 'yyyy-mm-dd'")
            ).andExpect(
                jsonPath("$.to").value("Date 'FROM' should format in 'yyyy-mm-dd'")
            ),
        () -> mvc.perform(
                post(SEARCH_TEMPLATE).content("{\"from\": \"12992312\", \"to\": \"12312312\"}"))
            .andExpect(status().isBadRequest()).andExpect(
                jsonPath("$.from").value("Date 'FROM' should format in 'yyyy-mm-dd'")
            ).andExpect(
                jsonPath("$.to").value("Date 'FROM' should format in 'yyyy-mm-dd'")
            ));
  }

  @Test
  void shouldReturnArrayWithThreeUsers() throws JsonProcessingException {
    List<User> testUsers = getTestUsers();
    when(service.searchByDate(any(), any())).thenReturn(testUsers);
    ObjectMapper mapper = new ObjectMapper();
    DateFilter filter = new DateFilter("1900-01-01", "2025-01-01");

    assertAll(() -> mvc.perform(get(SEARCH_TEMPLATE + "?from=1900-01-01&to=2025-01-01"))
            .andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length").value(2))
            .andExpect(jsonPath("$.[0]").value(mapper.writeValueAsString(testUsers.get(0))))
            .andExpect(jsonPath("$.[1]").value(mapper.writeValueAsString(testUsers.get(1))))
            .andExpect(jsonPath("$.[2]").value(mapper.writeValueAsString(testUsers.get(2)))),
        () -> mvc.perform(post(SEARCH_TEMPLATE).content(mapper.writeValueAsString(filter)))
            .andExpect(status().isOk()).andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$.length").value(2))
            .andExpect(jsonPath("$.[0]").value(mapper.writeValueAsString(testUsers.get(0))))
            .andExpect(jsonPath("$.[1]").value(mapper.writeValueAsString(testUsers.get(1))))
            .andExpect(jsonPath("$.[2]").value(mapper.writeValueAsString(testUsers.get(2)))));

  }

}