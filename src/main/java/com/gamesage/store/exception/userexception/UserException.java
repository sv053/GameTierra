package com.gamesage.store.exception.userexception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class UserException {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    public UserException(String message,
                         Throwable throwable,
                         HttpStatus httpStatus,
                         ZonedDateTime timestamp) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }
}

