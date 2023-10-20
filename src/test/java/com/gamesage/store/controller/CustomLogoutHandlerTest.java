package com.gamesage.store.controller;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.util.TokenParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.mockito.Mockito.*;

class CustomLogoutHandlerTest {

    @Mock
    private Authentication authentication;
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;
    @Mock
    private AuthService authService;
    @Mock
    private TokenService tokenService;

    private final Integer userId = 123;
    private final String headerWithToken = "123&goierjgodfv";
    private AuthToken validAuthToken;


    @BeforeEach
    void setUp() {
        validAuthToken = new AuthToken(headerWithToken, userId);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogout_validToken() {

        when(request.getHeader(HeaderName.TOKEN_HEADER)).thenReturn(headerWithToken);
        when(tokenService.findTokenByUserId(userId)).thenReturn(Optional.of(validAuthToken));

        CustomLogoutHandler customLogoutHandler = new CustomLogoutHandler(authService, tokenService);
        customLogoutHandler.logout(request, response, authentication);

        String validToken = TokenParser.findTokenValue(headerWithToken);
        verify(authService).revokeAccess(new AuthToken(validToken, validAuthToken.getUserId()));
    }

    @Test
    void testLogout_invalidToken() {
        String invalidToken = "000&fyt";
        AuthToken invalidAuthToken = new AuthToken(invalidToken, userId);
        when(request.getHeader(HeaderName.TOKEN_HEADER)).thenReturn(headerWithToken);
        when(tokenService.findTokenByUserId(userId)).thenReturn(Optional.of(validAuthToken));

        CustomLogoutHandler customLogoutHandler = new CustomLogoutHandler(authService, tokenService);
        customLogoutHandler.logout(request, response, authentication);

        verify(authService, never()).revokeAccess(invalidAuthToken);
    }
}