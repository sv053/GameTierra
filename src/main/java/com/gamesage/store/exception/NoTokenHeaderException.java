package com.gamesage.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no token header was given")
public class NoTokenHeaderException extends RuntimeException {

    public NoTokenHeaderException() {
        super("No header with token has been found");
    }
}

