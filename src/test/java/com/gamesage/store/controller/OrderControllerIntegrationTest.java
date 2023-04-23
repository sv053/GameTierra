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
import java.util.List;

import static java.nio.file.Path.of;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerIntegrationTest extends ControllerIntegrationTest {

    private static final String API_ORDER_ENDPOINT = "/cart";
    private static final String ORDER_ID_ENDPOINT = "/cart/{id}";
    private static final String ORDER_BUY_ENDPOINT = "/cart/{gameId}/{userId}";
    private static final String WRONG_TOKEN_HEADER = "unknownTokenValue";
    private static final String TOKEN_HEADER_TITLE = "X-Auth-Token";

    private Game game;
    private List<Game> savedGames;
    private String token;

    @Autowired
    private OrderService orderService;

    @BeforeAll
    void setup() throws Exception {
        userJson = Files.readString(of(userJsonResource.getURI()));
        User userToSave = objectMapper.readValue(userJson, User.class);
        user = userService.createOne(userToSave);
        token = loginAndGetToken(userJson);
        savedGames = gameService.createAll(List.of(SampleData.GAMES.get(0), SampleData.GAMES.get(1)));
        game = savedGames.get(0);
    }

    @Test
    void givenWrongCreds_shouldNotFindOrderByIdAndReturn401() throws Exception {
        int wrongId = -15;
        mockMvc.perform(get(ORDER_ID_ENDPOINT, wrongId)
                        .header(TOKEN_HEADER_TITLE, WRONG_TOKEN_HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenUnknownUser_shouldNotFindOrderByIdAndReturn403() throws Exception {
        int wrongId = -15;
        mockMvc.perform(get(ORDER_ID_ENDPOINT, wrongId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenRightCreds_shouldFindAllOrders() throws Exception {
        orderService.buyGame(game.getId(), user.getId());
        orderService.buyGame(savedGames.get(1).getId(), user.getId());

        mockMvc.perform(get(API_ORDER_ENDPOINT)
                        .header(TOKEN_HEADER_TITLE, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*.game.name", containsInAnyOrder(
                        game.getName(), savedGames.get(1).getName())))
                .andExpect(jsonPath("$.*.user.login").isNotEmpty());
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
                .andExpect(status().isUnauthorized());
    }
}

