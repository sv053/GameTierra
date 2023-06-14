package com.gamesage.store.controller;

import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/logout")
public class LogoutController {

    private final AuthService authService;
    private final TokenService tokenService;

    public LogoutController(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String tokenFromHeader = request.getHeader(HeaderName.TOKEN_HEADER);
        Integer userId = Integer.parseInt(tokenFromHeader.split("^")[0]);
        if (!tokenFromHeader.isEmpty() && tokenService.findTokenByUserId(userId).isPresent()) {
            authService.revokeAccess(tokenFromHeader);
            SecurityContextHolder.clearContext();

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

