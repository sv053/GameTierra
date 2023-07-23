package com.gamesage.store.controller;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.util.TokenParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class CustomLogoutHandler implements LogoutHandler {

        @Autowired
        private AuthService authService;
        @Autowired
        private TokenService tokenService;

        @Override
        public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
                String tokenFromHeader = request.getHeader(HeaderName.TOKEN_HEADER);

                if (Objects.nonNull(tokenFromHeader)) {
                        int userId = -1;
                        String idFromToken = TokenParser.findStringPart(tokenFromHeader, TokenParser.USER_ID_PART_NUMBER);
                        if (Objects.nonNull(idFromToken)) {
                                userId = Integer.parseInt(idFromToken);
                        }
                        if (!tokenFromHeader.isEmpty() && tokenService.findTokenByUserId(userId).isPresent()) {
                                String token = TokenParser.findStringPart(tokenFromHeader, TokenParser.TOKEN_VALUE_PART_NUMBER);
                                AuthToken authToken = new AuthToken(token, userId);
                                authService.revokeAccess(authToken);
                        }
                }
        }
}
