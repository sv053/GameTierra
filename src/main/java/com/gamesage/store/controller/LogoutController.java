package com.gamesage.store.controller;

import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    private final AuthService authService;

    public LogoutController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping
    public ResponseEntity<String> logout(HttpServletRequest request) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        String tokenFronHeader = request.getHeader(HeaderName.TOKEN_HEADER);
        if (!tokenFronHeader.isEmpty()) {
            authService.revokeAccess(tokenFronHeader);
            return ResponseEntity.ok()
                    .header(HeaderName.TOKEN_HEADER, "")
                    .body("You have successfully logged out");
        } else {
            return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED)
                    .header(HeaderName.TOKEN_HEADER, "")
                    .body("You are logged out already");
        }
    }
}

