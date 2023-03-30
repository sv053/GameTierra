package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class LoginControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final String API_ENDPOINT = "/login";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ObjectMapper objectMapper;

    private String userJson;

    @BeforeEach
    void setup() {
        userJson = "{\"id\": 2, " +
                " \"login\": \"admin\", " +
                " \"password\": \"letmein\", " +
                " \"tier\": { " +
                " \"cashbackPercentage\": 0.0, " +
                " \"id\": 1, " +
                " \"level\": \"FREE\" " +
                "}, " +
                "\"balance\": 815.16, " +
                "\"games\": []}";
    }

    @Test
    void givenCorrectCreds_shouldLoginAndReturn200() throws Exception {
        User userWithRightCreds = new User(111, "admin", "letmein", new Tier(
                1, "FREE", 0.d), BigDecimal.TEN);

        userService.createOne(userWithRightCreds);

        String tokenResponseValue = mockMvc.perform(post(API_ENDPOINT)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader("X-Auth-Token");

        Optional<AuthToken> token = tokenService.findTokenByLogin(userWithRightCreds.getLogin());
        String tokenValue = "";
        if (token.isPresent()) {
            tokenValue = token.get().getValue();
        }

        assertNotNull(tokenResponseValue);
        assertEquals(tokenValue, tokenResponseValue);
    }

    @Test
    void givenUserDoesNotExist_shouldNotLoginAndReturn401() throws Exception {
        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(userJson)
                        .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    void givenCorrectLoginAndWrongKey_shouldReturn401() throws Exception {
        User user = objectMapper.readValue(userJson, User.class);
        User savedUser = userService.createOne(user);
        String passValue = savedUser.getPassword();

        userJson = "{\"id\": 2, " +
                " \"login\": \"admin\", " +
                " \"password\": \" " + passValue + " \", " +
                " \"tier\": { " +
                " \"cashbackPercentage\": 0.0, " +
                " \"id\": 1, " +
                " \"level\": \"FREE\" " +
                "}, " +
                "\"balance\": 815.16, " +
                "\"games\": []}";

        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(userJson)
                        .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    void givenWrongLoginAndCorrectKey_shouldReturn401() throws Exception {
        User userWithWrongCreds = new User(2, "hijacker", "letmein", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        String userJson = objectMapper.writeValueAsString(userWithWrongCreds);

        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(userJson)
                        .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}

