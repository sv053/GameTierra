package com.gamesage.store.controller;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class LoginControllerIntegrationTest {

    private static final String API_ENDPOINT = "/login";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;

    private final Tier tier = new Tier(5, "PLATINUM", 30.0);
    private final User user = new User(1, "testuser", "testpass", tier,
            BigDecimal.valueOf(1000));
    private final String userJson = user.toString();

    @Test
    void givenCorrectCreds_shouldLoginAndReturn200() throws Exception {
        userService.createOne(user);
        String tokenHeaderName = "X-Auth-Token";
        String tokenResponseValue = mockMvc.perform(post(API_ENDPOINT)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(tokenHeaderName);

        Optional<AuthToken> token = tokenService.findTokenByLogin(user.getLogin());
        String tokenValue = token.isPresent() ? token.get().getValue() : "";

        assertNotNull(tokenResponseValue);
        assertEquals(tokenValue, tokenResponseValue);
    }

    @Test
    void givenUserCredsDoNotExist_shouldNotLoginAndReturn401() throws Exception {
        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    void givenCorrectLoginAndWrongCreds_shouldReturn401() throws Exception {
        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isUnauthorized());
    }
}

