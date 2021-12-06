package com.gamesage.store.exception.userexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no such user")
public class UserNotFoundException extends IllegalArgumentException{

    public UserNotFoundException(int id){
        super(String.format("Game with id %s not found", id));
    }

    public UserNotFoundException(String s) {
        super(s);
    }
}

