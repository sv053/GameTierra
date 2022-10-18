package com.gamesage.store.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public class Authenticator extends UsernamePasswordAuthentication {

    private void processUsernamePasswordAuthentication(HttpServletResponse httpResponse, Optional username, Optional password) throws IOException {
        Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password);
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        TokenResponse tokenResponse = new TokenResponse(resultOfAuthentication.getDetails().toString());
        String tokenJsonResponse = new ObjectMapper().writeValueAsString(tokenResponse);
        httpResponse.addHeader("Content-Type", "application/json");
        httpResponse.getWriter().print(tokenJsonResponse);
    }

    private Authentication tryToAuthenticateWithUsernameAndPassword(Optional username, Optional password) {
        UsernamePasswordAuthenticationToken requestAuthentication = new UsernamePasswordAuthenticationToken(username, password);
        return tryToAuthenticate(requestAuthentication);
    }
}
