package com.gamesage.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.Tier;
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

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginControllerIntegrationTest {

    private static final String API_ENDPOINT = "/login";
    private static final String TOKEN_HEADER_NAME = "X-Auth-Token";
    private static String USER_JSON;

    private final Tier tier = new Tier(5, "PLATINUM", 30.0);
    private final User user = new User(null, "testuser", "testpass", tier,
            BigDecimal.valueOf(1000));

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setup() throws JsonProcessingException {

        USER_JSON = objectMapper.writeValueAsString(user);
    }

    @Test
    void givenCorrectCreds_shouldLoginAndReturn200() throws Exception {
        userService.createOne(user);
        String tokenResponseValue = mockMvc.perform(post(API_ENDPOINT)
                        .content(USER_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(TOKEN_HEADER_NAME);

        String tokenValue = tokenService.findTokenByLogin(user.getLogin())
                .map(AuthToken::getValue)
                .orElse("");

        assertNotEquals("", tokenValue);
        assertEquals(tokenValue, tokenResponseValue);
    }

    @Test
    void givenUserCredsDoNotExist_shouldNotLoginAndReturn401() throws Exception {
        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}

