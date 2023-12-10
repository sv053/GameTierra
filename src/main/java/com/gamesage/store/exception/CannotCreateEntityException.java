package com.gamesage.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "cannot create entity")
public class CannotCreateEntityException extends RuntimeException {

    public CannotCreateEntityException() {
        super("Impossible to create an entity");
    }

    public CannotCreateEntityException(String msg) {
        super(msg);
    }
}

