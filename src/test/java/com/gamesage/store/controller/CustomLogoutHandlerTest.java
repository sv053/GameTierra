package com.gamesage.store.controller;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.exception.WrongCredentialsException;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.TokenService;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.mockito.Mockito.*;

class CustomLogoutHandlerTest {

    private final Authentication authentication = mock(Authentication.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final AuthService authService = mock(AuthService.class);
    private final TokenService tokenService = mock(TokenService.class);

    private final Integer userId = 123;
    private final String headerWithToken = "123&goierjgodfv";

    private final AuthToken authToken = new AuthToken(headerWithToken, userId);

    @Test
    void testLogout_validToken() {
        when(request.getHeader(HeaderName.TOKEN_HEADER)).thenReturn(headerWithToken);
        when(tokenService.findTokenByUserId(userId)).thenReturn(Optional.of(authToken));

        CustomLogoutHandler customLogoutHandler = new CustomLogoutHandler(authService, tokenService);
        customLogoutHandler.logout(request, response, authentication);

        String validToken = "goierjgodfv";
        verify(authService).revokeAccess(new AuthToken(validToken, userId));
    }

    @Test
    void testLogout_invalidToken() {
        String invalidToken = "123&fyt";
        AuthToken authToken = new AuthToken(invalidToken, userId);
        when(request.getHeader(HeaderName.TOKEN_HEADER)).thenReturn(invalidToken);
        when(tokenService.findTokenByUserId(userId)).thenReturn(Optional.of(authToken));
        when(tokenService.invalidateToken(authToken))
            .thenThrow(new WrongCredentialsException());

        CustomLogoutHandler customLogoutHandler = new CustomLogoutHandler(authService, tokenService);
        customLogoutHandler.logout(request, response, authentication);

        verify(authService, never()).revokeAccess(authToken);
    }
}