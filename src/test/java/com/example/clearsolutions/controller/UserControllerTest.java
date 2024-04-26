package com.example.clearsolutions.controller;


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

import com.example.clearsolutions.exception.UserServiceException;
import com.example.clearsolutions.model.User;
import com.example.clearsolutions.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    doThrow(UserServiceException.class).when(service).delete(any(Long.class));
    doThrow(UserServiceException.class).when(service).update(any(Long.class), any(User.class));
    doThrow(UserServiceException.class).when(service)
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
  void shouldReturnArrayWithTwoSearchedDates() {


    when(service.searchByDate(any(), any())).then()
    assertAll();


  }

  @Test
  void shouldReturnNotValidAgeForRegistration() {

  }

  @Test
  void shouldReturnInvalidDataWhenUpdateRequiredFields() {

  }

}