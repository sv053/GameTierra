package com.gamesage.store.security.auth.filter;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.util.TokenParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class PreAuthenticationFilter extends AbstractPreAuthenticatedProcessingFilter {

    @Autowired
    TokenService tokenService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HeaderName.TOKEN_HEADER);
        if (null == token || token.isEmpty()) {
            return null;
        }
        int expectedUserId = TokenParser.findUserId(token);
        if (0 >= expectedUserId) {
            return null;
        }
        Optional<AuthToken> authToken = tokenService.findTokenByUserId(expectedUserId);
        if (authToken.isPresent()) {
            String encodedToken = authToken.get().getValue();
            String rawToken = TokenParser.findTokenValue(token);
            if (encoder.matches(rawToken, encodedToken)) {
                return encodedToken;
            }
        }
        return null;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(HeaderName.TOKEN_HEADER);
    }
}

