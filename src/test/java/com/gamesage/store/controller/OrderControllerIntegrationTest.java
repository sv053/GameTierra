package com.gamesage.store.controller;

import com.gamesage.store.domain.data.SampleData;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Order;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerIntegrationTest extends ControllerIntegrationTest {

    private static final String API_ORDER_ENDPOINT = "/cart";
    private static final String ORDER_ID_ENDPOINT = "/cart/{id}";
    private static final String ORDER_BUY_ENDPOINT = "/cart/{gameId}/{userId}";
    private static final String WRONG_TOKEN_HEADER = "111111&unknownTokenValue";
    private static final String TOKEN_HEADER_TITLE = "X-Auth-Token";

    private String userJson;
    private User user;
    private Game game;
    private List<Game> savedGames;
    private String token;

    @Autowired
    private OrderService orderService;

    @BeforeAll
    void setup() throws Exception {
        userJson = Files.readString(Path.of(userJsonResource.getURI()));
        User userToSave = objectMapper.readValue(userJson, User.class);
        user = userService.createOne(userToSave);
        token = loginAndGetToken(userJson);
        savedGames = gameService.createAll(SampleData.GAMES.subList(0, 2));
        game = savedGames.get(0);
    }

    @Test
    void givenWrongCreds_shouldNotFindOrderById() throws Exception {
        int wrongId = -15;
        mockMvc.perform(get(ORDER_ID_ENDPOINT, wrongId)
                .header(TOKEN_HEADER_TITLE, WRONG_TOKEN_HEADER)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    void givenUnknownUser_shouldNotFindOrderById() throws Exception {
        int wrongId = -15;
        mockMvc.perform(get(ORDER_ID_ENDPOINT, wrongId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    void givenRightCreds_shouldFindAllOrders() throws Exception {
        orderService.buyGame(game.getId(), user.getId());
        orderService.buyGame(savedGames.get(1).getId(), user.getId());

        var v = mockMvc.perform(get(API_ORDER_ENDPOINT)
                .header(TOKEN_HEADER_TITLE, token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].user.login").value(user.getLogin()))
            .andExpect(jsonPath("$[0].user.id").value(user.getId()))
            .andExpect(jsonPath("$[0].game.name").value(game.getName()))
            .andExpect(jsonPath("$[0].game.id").value(game.getId()));
    }

    @Test
    void givenRightCreds_shouldFindOrderById() throws Exception {
        orderService.buyGame(game.getId(), user.getId());

        Order order = orderService.findAll().get(0);

        mockMvc.perform(get(ORDER_ID_ENDPOINT, order.getId())
                .header(TOKEN_HEADER_TITLE, token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.login").value(user.getLogin()))
            .andExpect(jsonPath("$.user.id").value(order.getUser().getId()))
            .andExpect(jsonPath("$.game.name").value(game.getName()))
            .andExpect(jsonPath("$.game.id").value(game.getId()));
    }

    @Test
    void givenRightCreds_shouldBuyGame() throws Exception {
        mockMvc.perform(post(ORDER_BUY_ENDPOINT, game.getId(), user.getId())
                .header(TOKEN_HEADER_TITLE, token)
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.bought").value(true))
            .andExpect(jsonPath("$.buyer.id").value(user.getId()))
            .andExpect(jsonPath("$.buyer.login").value(user.getLogin()))
            .andExpect(jsonPath("$.targetGame.name").value(game.getName()));
    }

    @Test
    void givenWrongCreds_shouldNotBuyGame() throws Exception {
        mockMvc.perform(post(ORDER_BUY_ENDPOINT, game.getId(), user.getId())
                .header(TOKEN_HEADER_TITLE, WRONG_TOKEN_HEADER)
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }

    @Test
    void givenEmptyToken_shouldNotBuyGame() throws Exception {
        mockMvc.perform(post(ORDER_BUY_ENDPOINT, game.getId(), user.getId())
                .header(TOKEN_HEADER_TITLE, "")
                .content(userJson)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }
}

