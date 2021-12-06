package com.gamesage.store.exception.gameexception;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class GameException {
    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    public GameException(String message,
                         Throwable throwable,
                         HttpStatus httpStatus,
                         ZonedDateTime timestamp) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }
}

