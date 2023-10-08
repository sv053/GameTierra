package com.gamesage.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "wrong token")
public class WrongTokenException extends RuntimeException {

	public WrongTokenException(String header) {
		super(String.format("Token does not meet requirements %s ___", header));
	}
}

