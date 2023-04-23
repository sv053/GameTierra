package com.gamesage.store.controller;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.TokenService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import static java.nio.file.Path.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerIntegrationTest extends ControllerIntegrationTest {

    @Autowired
    private TokenService tokenService;

    @BeforeAll
    void setup() throws IOException {
        userJson = Files.readString(of(userJsonResource.getURI()));
        user = objectMapper.readValue(userJson, User.class);
    }

    @Test
    void givenCorrectCreds_shouldLoginAndReturn200() throws Exception {
        userService.createOne(user);
        String tokenResponseValue = loginAndGetToken(userJson);

        Optional<String> tokenValue = tokenService.findTokenByLogin(user.getLogin())
                .map(AuthToken::getValue);

        assertEquals(tokenValue, Optional.of(tokenResponseValue));
    }

    @Test
    void givenUserCredsDoNotExist_shouldNotLoginAndReturn401() throws Exception {
        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized());
    }
}

