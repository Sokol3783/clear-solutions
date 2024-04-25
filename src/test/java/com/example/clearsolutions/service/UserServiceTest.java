package com.example.clearsolutions.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserServiceTest {

  @Test
  void shouldThrowUserNotFoundExceptionWhenThereAreNoUsers(){
      fail();
  }


  @Test
  void shouldThrowInvalidDateFilter(){
    fail();
  }

  @Test
  void shouldCreateUser(){ fail(); }

  @Test
  void shouldThrowUserAlreadyCreated(){fail();}

  @Test
  void shouldThrowValidateExceptionWhenUpdateRequiredFieldsNotValid(){ fail(); }

  @Test
  void shouldThrowThatAgeIsLessThanRequired(){ fail(); }

  @Test
  void shouldReturnEmptyListBecauseDateOf() { fail(); }

  @Test
  void shouldReturnAllDatesFromSearch() {fail();}

  @Test
  void shouldReturnTwoDatesFromSearch() {fail();}

}