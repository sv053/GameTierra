package com.gamesage.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "there was no token")
public class NoTokenException extends RuntimeException {

public NoTokenException(String header) {
	super(String.format("Token not found in header %s ___", header));
}
}
