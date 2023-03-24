package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.GameService;
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
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class OrderControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final String API_LOGIN_ENDPOINT = "/login";
    private static final String API_ORDER_ENDPOINT = "/order";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private GameService gameService;

    @Test
    void givenRightCreds_shouldFindOrderById() throws Exception {
        User userWithRightCreds = new User(111, "admin", "letmein", new Tier(
                1, "FREE", 0.d), BigDecimal.TEN);

        User savedUser = userService.createOne(userWithRightCreds);

        mockMvc.perform(post(API_LOGIN_ENDPOINT)
                        .content(new ObjectMapper().writeValueAsString(userWithRightCreds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<AuthToken> token = tokenService.findTokenByLogin(savedUser.getLogin());

        String tokenValue = "";
        if (token.isPresent()) {
            tokenValue = token.get().getValue();
        }

        mockMvc.perform(get(API_ORDER_ENDPOINT + "/1")
                        .header("X-Auth-Token", tokenValue)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    void givenWrongCreds_shouldNotFindOrderById() throws Exception {
        User userWithRightCreds = new User(111, "admin", "letmein", new Tier(
                1, "FREE", 0.d), BigDecimal.TEN);

        User savedUser = userService.createOne(userWithRightCreds);

        mockMvc.perform(post(API_LOGIN_ENDPOINT)
                        .content(new ObjectMapper().writeValueAsString(userWithRightCreds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Game game = new Game(2, "THE_LAST_OF_US", BigDecimal.valueOf(7.28d));
        Game savedGame = gameService.createOne(game);

        mockMvc.perform(get(API_ORDER_ENDPOINT + "/{gameId}/{userId}", savedGame.getId(), savedUser.getId())
                        .content(new ObjectMapper().writeValueAsString(savedUser))
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(API_ORDER_ENDPOINT + "/1")
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenRightCreds_shouldFindAllOrders() throws Exception {
        User userWithRightCreds = new User(111, "admin", "letmein", new Tier(
                1, "FREE", 0.d), BigDecimal.TEN);

        User savedUser = userService.createOne(userWithRightCreds);

        mockMvc.perform(post(API_LOGIN_ENDPOINT)
                        .content(new ObjectMapper().writeValueAsString(userWithRightCreds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Game game = new Game(2, "THE_LAST_OF_US", BigDecimal.valueOf(7.28d));
        Game savedGame = gameService.createOne(game);

        Optional<AuthToken> token = tokenService.findTokenByLogin(savedUser.getLogin());

        String tokenValue = "";
        if (token.isPresent()) {
            tokenValue = token.get().getValue();
        }

        mockMvc.perform(get(API_ORDER_ENDPOINT + "/{gameId}/{userId}", savedGame.getId(), savedUser.getId())
                        .header("X-Auth-Token", tokenValue)
                        .content(new ObjectMapper().writeValueAsString(savedUser))
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        mockMvc.perform(get(API_ORDER_ENDPOINT)
                        .header("X-Auth-Token", tokenValue)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    void givenRightCreds_shouldBuyGame() throws Exception {
        User userWithRightCreds = new User(111, "admin", "letmein", new Tier(
                1, "FREE", 0.d), BigDecimal.TEN);

        User savedUser = userService.createOne(userWithRightCreds);

        mockMvc.perform(post(API_LOGIN_ENDPOINT)
                        .content(new ObjectMapper().writeValueAsString(userWithRightCreds))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<AuthToken> token = tokenService.findTokenByLogin(userWithRightCreds.getLogin());

        String tokenValue = "";
        if (token.isPresent()) {
            tokenValue = token.get().getValue();
        }
        Game game = new Game(2, "THE_LAST_OF_US", BigDecimal.valueOf(7.28d));
        Game savedGame = gameService.createOne(game);

        mockMvc.perform(get(API_ORDER_ENDPOINT + "/{gameId}/{userId}", savedGame.getId(), savedUser.getId())
                        .header("X-Auth-Token", tokenValue)
                        .content(new ObjectMapper().writeValueAsString(savedUser))
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    void givenWrongCreds_shouldNotBuyGame() throws Exception {
        User userWithRightCreds = new User(111, "admin", "letmein", new Tier(
                1, "FREE", 0.d), BigDecimal.TEN);

        User savedUser = userService.createOne(userWithRightCreds);

        mockMvc.perform(post(API_LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        Game game = new Game(2, "THE_LAST_OF_US", BigDecimal.valueOf(7.28d));

        Game savedGame = gameService.createOne(game);

        mockMvc.perform(get(API_ORDER_ENDPOINT + "/{gameId}/{userId}", savedGame.getId(), savedUser.getId())
                        .content(new ObjectMapper().writeValueAsString(userWithRightCreds))
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized());
    }
}

