package com.gamesage.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "no such credentials")
public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("User with such login and password already exists");
    }
}

