package com.gamesage.store.security.auth.filter;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.security.auth.HeaderName;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.util.Parser;
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
        String delimiter = "$$";
        String regexDelimiter = "\\$\\$";
        int idFromToken = Integer.parseInt(Parser.findSubstring(token, delimiter, regexDelimiter, 0));
        Optional<AuthToken> authToken = tokenService.findTokenByUserId(idFromToken);
        if (authToken.isPresent()) {
            String encodedToken = authToken.get().getValue();
            int substringIndex = token.indexOf(delimiter) + delimiter.length();
            String rawToken = token.substring(substringIndex);
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

