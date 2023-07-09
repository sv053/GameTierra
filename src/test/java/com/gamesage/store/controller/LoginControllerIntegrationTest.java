package com.gamesage.store.controller;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.EntityNotFoundException;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.util.Parser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerIntegrationTest extends ControllerIntegrationTest {

    private String userJson;
    private User user;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @BeforeAll
    void setup() throws IOException {
        userJson = Files.readString(Path.of(userJsonResource.getURI()));
        user = objectMapper.readValue(userJson, User.class);
    }

    @Test
    void givenCorrectCreds_shouldLoginAndReturn200() throws Exception {
        User savedUser = userService.createOne(user);
        String actualToken = loginAndGetToken(userJson);
        String tokenWithoutUserId = Parser.findSubstring(actualToken, 1);

        String expectedToken = tokenService
                .findTokenByUserId(savedUser.getId())
                .map(AuthToken::getValue)
                .orElseThrow(() -> new EntityNotFoundException("token for " + user.getLogin()));

        assertTrue(encoder.matches(tokenWithoutUserId, expectedToken));
    }

    @Test
    void givenUserCredsDoNotExist_shouldNotLoginAndReturn401() throws Exception {
        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized());
    }
}

