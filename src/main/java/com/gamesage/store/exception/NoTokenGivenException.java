package com.gamesage.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no token was given")
public class NoTokenGivenException extends RuntimeException {

    public NoTokenGivenException(String header) {
        super(String.format("Token not found in header %s ___", header));
    }
}

