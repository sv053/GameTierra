package com.gamesage.store.controller;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.EntityNotFoundException;
import com.gamesage.store.service.TokenService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerIntegrationTest extends ControllerIntegrationTest {

    @Value("classpath:request/user/existentUser.json")
    private Resource userJsonResource;
    private String userJson;
    private User user;

    @Autowired
    private TokenService tokenService;

    @BeforeAll
    void setup() throws IOException {
        userJson = Files.readString(Path.of(userJsonResource.getURI()));
        user = objectMapper.readValue(userJson, User.class);
    }

    @Test
    void givenCorrectCreds_shouldLoginAndReturn200() throws Exception {
        userService.createOne(user);
        String tokenResponseValue = loginAndGetToken(userJson);

        String tokenValue = tokenService.findTokenByLogin(user.getLogin())
                .map(AuthToken::getValue)
                .orElseThrow(() -> new EntityNotFoundException("token for " + user.getLogin()));

        assertEquals(tokenValue, tokenResponseValue);
    }

    @Test
    void givenUserCredsDoNotExist_shouldNotLoginAndReturn401() throws Exception {
        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized());
    }

    @AfterAll
    void tearDown() {
        userService.deleteAll();
    }
}

