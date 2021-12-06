package com.gamesage.store.exception.gameexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "no such user")
public class GameNotFoundException extends IllegalArgumentException{

    public GameNotFoundException(int id){
        super(String.format("Game with id %s not found", id));
    }
}

