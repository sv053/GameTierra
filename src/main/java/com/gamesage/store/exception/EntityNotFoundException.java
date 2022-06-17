package com.gamesage.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no such entity")
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(int id) {
        super(String.format("Entity with id %d not found", id));
    }

    public EntityNotFoundException(String login) {
        super(String.format("Entity with login %s not found", login));
    }
}

