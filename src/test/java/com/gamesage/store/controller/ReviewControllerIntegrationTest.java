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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerIntegrationTest extends ControllerIntegrationTest {

    private static final String API_REVIEW_ENDPOINT = "/reviews";
    private static final String REVIEW_ID_ENDPOINT = "/reviews/{id}";
    private static final String ORDER_BUYREVIEW_ENDPOINT = "/reviews/{gameId}/{userId}";
    private static final String WRONG_TOKEN_HEADER = "unknownTokenValue";
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
    void givenWrongCreds_shouldNotFindReviewById() throws Exception {
        int wrongId = -15;
        mockMvc.perform(get(REVIEW_ID_ENDPOINT, wrongId)
                        .header(TOKEN_HEADER_TITLE, WRONG_TOKEN_HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenUnknownUser_shouldNotFindReviewById() throws Exception {
        int wrongId = -15;
        mockMvc.perform(get(REVIEW_ID_ENDPOINT, wrongId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenRightCreds_shouldFindAllReviews() throws Exception {
        orderService.buyGame(game.getId(), user.getId());
        orderService.buyGame(savedGames.get(1).getId(), user.getId());

        mockMvc.perform(get(REVIEW_ID_ENDPOINT)
                        .header(TOKEN_HEADER_TITLE, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*.game.name", containsInAnyOrder(
                        game.getName(), savedGames.get(1).getName())))
                .andExpect(jsonPath("$.*.user.login").isNotEmpty());
    }

    @Test
    void givenRightCreds_shouldFindReviewById() throws Exception {
        orderService.buyGame(game.getId(), user.getId());

        Order order = orderService.findAll().get(0);

        mockMvc.perform(get(REVIEW_ID_ENDPOINT, order.getId())
                        .header(TOKEN_HEADER_TITLE, token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.login").value(user.getLogin()))
                .andExpect(jsonPath("$.user.id").value(order.getUser().getId()))
                .andExpect(jsonPath("$.game.name").value(game.getName()))
                .andExpect(jsonPath("$.game.id").value(game.getId()));
    }

    @Test
    void givenRightCreds_shouldWriteReview() throws Exception {
        mockMvc.perform(post(REVIEW_ID_ENDPOINT, game.getId(), user.getId())
                        .header(TOKEN_HEADER_TITLE, token)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(true))
                .andExpect(jsonPath("$.gameId").value(user.getId()));
    }

    @Test
    void givenWrongCreds_shouldNotWriteReview() throws Exception {
        mockMvc.perform(post(REVIEW_ID_ENDPOINT, game.getId(), user.getId())
                        .header(TOKEN_HEADER_TITLE, WRONG_TOKEN_HEADER)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void hasNotGame_shouldNotWriteReview() throws Exception {
        mockMvc.perform(post(REVIEW_ID_ENDPOINT, game.getId(), user.getId())
                        .header(TOKEN_HEADER_TITLE, WRONG_TOKEN_HEADER)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}

