package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameService gameService;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_GAME_ENDPOINT = "/games";
    private final String SLASH = "/";
    private final Game game = new Game("THE_LAST_OF_US", BigDecimal.valueOf(7.28d));
    private final Game anotherGame = new Game("THE_WITCHER", BigDecimal.valueOf(17.28d));
    private final List<Game> games = Arrays.asList(game, anotherGame);

    @Test
    void shouldFindGameById() throws Exception {
        Game createdGame = gameService.createOne(game);
        mockMvc.perform(get(API_GAME_ENDPOINT + SLASH + createdGame.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdGame.getId()))
                .andExpect(jsonPath("$.name").value(createdGame.getName()))
                .andExpect(jsonPath("$.price").value(createdGame.getPrice()));
    }

    @Test
    void shouldThrowException_whenTryToFindGameById_IdDoesNotExist() throws Exception {
        int wrongId = -888;
        mockMvc.perform(get(API_GAME_ENDPOINT + SLASH + wrongId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFindAllGames() throws Exception {
        List<Game> savedGames = gameService.createAll(games);

        ResultActions resultActions = mockMvc.perform(get(API_GAME_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        for (int i = 0; i < savedGames.size(); i++) {
            Game savedGame = savedGames.get(i);
            String jsonPathPrefix = String.format("$[%d].", i);
            resultActions.andExpect(jsonPath(jsonPathPrefix + "id").value(savedGame.getId()))
                    .andExpect(jsonPath(jsonPathPrefix + "name").value(savedGame.getName()))
                    .andExpect(jsonPath(jsonPathPrefix + "price").value(savedGame.getPrice()));
        }
    }

    @Test
    void shouldCreateGamesWithUserRightCreds() throws Exception {
        String gamesToSaveJson = objectMapper.writeValueAsString(games);
        ResultActions resultActions = mockMvc.perform(post(API_GAME_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gamesToSaveJson));

        for (int i = 0; i < games.size(); i++) {
            Game savedGame = games.get(i);
            String jsonPathPrefix = String.format("$[%d].", i);
            resultActions.andExpect(jsonPath(jsonPathPrefix + "name").value(savedGame.getName()))
                    .andExpect(jsonPath(jsonPathPrefix + "price").value(savedGame.getPrice()));
        }
    }
}

