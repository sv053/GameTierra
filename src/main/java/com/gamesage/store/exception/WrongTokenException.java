package com.gamesage.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "wrong token")
public class WrongTokenException extends RuntimeException {

    public WrongTokenException() {
        super("Token does not meet requirements");
    }
}

