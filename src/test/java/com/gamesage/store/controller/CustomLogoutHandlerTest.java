package com.gamesage.store.controller;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.security.service.AuthService;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.util.TokenParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomLogoutHandlerTest {

    private final Integer userId = 123;
    private final String headerWithToken = "123&goierjgodfv";
    private final AuthToken validAuthToken = new AuthToken(headerWithToken, userId);

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

    @InjectMocks
    private CustomLogoutHandler customLogoutHandler;

    @Test
    void testLogout_validToken() {

        when(request.getHeader(HeaderName.TOKEN_HEADER)).thenReturn(headerWithToken);
        when(tokenService.findTokenByUserId(userId)).thenReturn(Optional.of(validAuthToken));

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

        customLogoutHandler.logout(request, response, authentication);

        verify(authService, never()).revokeAccess(invalidAuthToken);
    }
}