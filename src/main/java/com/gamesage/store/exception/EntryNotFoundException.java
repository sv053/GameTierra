package com.gamesage.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no such user")
public class EntryNotFoundException extends RuntimeException {

    public EntryNotFoundException(int id) {
        super(String.format("Entry with id %s not found", id));
    }
}

