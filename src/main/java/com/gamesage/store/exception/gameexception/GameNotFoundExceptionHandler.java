package com.gamesage.store.exception.gameexception;

import com.gamesage.store.exception.userexception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class GameNotFoundExceptionHandler {
    public ResponseEntity<Object> handleGameNotFoundException(UserNotFoundException e){

        GameException userException = new GameException(
                e.getMessage(),
                e,
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(userException, HttpStatus.NOT_FOUND);
    }
}

