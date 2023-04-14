package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginControllerIntegrationTest {

    private static final String API_ENDPOINT = "/login";
    private static final String TOKEN_HEADER_NAME = "X-Auth-Token";
    private static final String USER_JSON_FILE_PATH = "src/test/resources/notSavedUser.json";

    private String userJson;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setup() throws IOException {
        userJson = Files.readString(Path.of(USER_JSON_FILE_PATH));
    }

    @Test
    void givenCorrectCreds_shouldLoginAndReturn200() throws Exception {
        User user = objectMapper.readValue(userJson, User.class);
        userService.createOne(user);
        String tokenResponseValue = mockMvc.perform(post(API_ENDPOINT)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(TOKEN_HEADER_NAME);

        Optional<String> tokenValue = tokenService.findTokenByLogin(user.getLogin())
                .map(AuthToken::getValue);

        assertNotNull(tokenResponseValue);
        assertFalse(tokenResponseValue.isBlank());
        assertEquals(tokenValue, Optional.of(tokenResponseValue));
    }

    @Test
    void givenUserCredsDoNotExist_shouldNotLoginAndReturn401() throws Exception {
        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}

