package com.gamesage.store.controller;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.exception.WrongTokenException;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.util.TokenParser;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class CustomLogoutHandler implements LogoutHandler {

    private final AuthService authService;
    private final TokenService tokenService;

    public CustomLogoutHandler(AuthService authService, TokenService tokenService) {
        this.authService = authService;
        this.tokenService = tokenService;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String tokenFromHeader = request.getHeader(HeaderName.TOKEN_HEADER);

        Integer userId = TokenParser.findUserId(tokenFromHeader);
        Optional<AuthToken> savedToken = tokenService.findTokenByUserId(userId);
        if (savedToken.isPresent()) {
            String token = TokenParser.findTokenValue(tokenFromHeader);
            AuthToken authToken = new AuthToken(token, userId);
            authService.revokeAccess(authToken);
        } else {
            throw new WrongTokenException();
        }
    }
}
