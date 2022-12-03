package com.gamesage.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED, reason = "no such credentials")
public class WrongCredentialsException extends RuntimeException {

    public WrongCredentialsException() {
        super("Login or password does not exist");
    }
}

