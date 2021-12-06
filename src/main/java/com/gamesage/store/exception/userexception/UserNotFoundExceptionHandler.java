package com.gamesage.store.exception.userexception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class UserNotFoundExceptionHandler {
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e){

        UserException userException = new UserException(
                e.getMessage(),
                e,
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(userException, HttpStatus.NOT_FOUND);
    }
}

