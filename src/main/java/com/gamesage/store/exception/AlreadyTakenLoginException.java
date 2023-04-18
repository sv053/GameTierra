package com.gamesage.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "login is taken")
public class AlreadyTakenLoginException extends RuntimeException {

    public AlreadyTakenLoginException() {
        super("Sorry, this login is already taken");
    }
}

