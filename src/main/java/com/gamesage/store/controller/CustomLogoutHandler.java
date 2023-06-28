package com.gamesage.store.controller;

import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.util.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

                int userId = -1;
                String idFromToken = Parser.findSubstring(tokenFromHeader, "$$", "\\$\\$", 0);
                if (Objects.nonNull(idFromToken)) {
                        userId = Integer.parseInt(idFromToken);
                }
                if (!tokenFromHeader.isEmpty() && tokenService.findTokenByUserId(userId).isPresent()) {
                        authService.revokeAccess(userId);
                        SecurityContextHolder.clearContext();
//
//                        return ResponseEntity.ok()
//                                .header(HeaderName.TOKEN_HEADER, "")
//                                .body("You have successfully logged out");
//                } else {
//                        return ResponseEntity.status(HttpStatus.LENGTH_REQUIRED)
//                                .header(HeaderName.TOKEN_HEADER, "")
//                                .body("You logged out already");
                }
        }
}
