package com.gamesage.store.controller;

import com.gamesage.store.domain.data.SampleData;
import com.gamesage.store.domain.model.Game;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class GameControllerIntegrationTest extends ControllerIntegrationTest {

    private static final String API_GAME_ENDPOINT = "/games";
    private static final String GAME_ID_ENDPOINT = "/games/{gameId}";

    private List<Game> games;
    private Game game;

    @BeforeAll
    void setup() {
        games = gameService.createAll(SampleData.GAMES.subList(0, 3));
        game = games.get(0);
    }

    @Test
    void shouldFindGameById() throws Exception {
        mockMvc.perform(get(GAME_ID_ENDPOINT, game.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(game.getId()))
                .andExpect(jsonPath("$.name").value(game.getName()))
                .andExpect(jsonPath("$.price").value(game.getPrice()));
    }

    @Test
    void shouldThrowException_whenTryToFindGameById_IdDoesNotExist() throws Exception {
        int wrongId = -888;
        mockMvc.perform(get(GAME_ID_ENDPOINT, wrongId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFindAllGames() throws Exception {
        mockMvc.perform(get(API_GAME_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*.name", containsInAnyOrder(
                        game.getName(),
                        games.get(1).getName(),
                        games.get(2).getName())))
                .andExpect(jsonPath("$.*.id", containsInAnyOrder(
                        game.getId(),
                        games.get(1).getId(),
                        games.get(2).getId())));
    }

    @Test
    void shouldCreateGamesWithUserRightCreds() throws Exception {
        Game gameOne = SampleData.GAMES.get(5);
        Game gameTwo = SampleData.GAMES.get(6);
        String gamesJson = objectMapper.writeValueAsString(List.of(gameOne, gameTwo));
        Double firstGamePrice = gameOne.getPrice().doubleValue();
        Double secondGamePrice = gameTwo.getPrice().doubleValue();

        mockMvc.perform(post(API_GAME_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gamesJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*.name", containsInAnyOrder(gameOne.getName(), gameTwo.getName())))
                .andExpect(jsonPath("$.*.price", containsInAnyOrder(firstGamePrice, secondGamePrice)));
    }

    @AfterAll
    void tearDown() {
        gameService.deleteAll();
    }
}

