package com.gamesage.store.controller;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.service.GameService;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GameControllerIntegrationTest {

    private static final String API_GAME_ENDPOINT = "/games";
    private static final String GAME_ID_ENDPOINT = "/games/{gameId}";

    public String GAMES_JSON;
    private Game game;
    private Game anotherGame;
    private List<Game> games;
    private Game savedGame;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameService gameService;
    @Autowired
    private Gson gson;

    @BeforeAll
    void setUp() {
        game = new Game("THE_LAST_OF_US", BigDecimal.valueOf(7.28d));
        anotherGame = new Game("THE_WITCHER", BigDecimal.valueOf(17.28d));
        games = Arrays.asList(game, anotherGame);
        GAMES_JSON = gson.toJson(games);
    }

    @Test
    void shouldFindGameById() throws Exception {
        savedGame = gameService.createOne(game);
        mockMvc.perform(get(GAME_ID_ENDPOINT, savedGame.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedGame.getId()))
                .andExpect(jsonPath("$.name").value(savedGame.getName()))
                .andExpect(jsonPath("$.price").value(savedGame.getPrice()));
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
        List<Game> savedGames = gameService.createAll(games);

        ResultActions resultActions = mockMvc.perform(get(API_GAME_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect((jsonPath("$.*.name", Matchers.containsInAnyOrder(game.getName(), anotherGame.getName()))))
                .andExpect((jsonPath("$.*.id", Matchers.containsInAnyOrder(savedGames.get(0).getId(),
                        savedGames.get(1).getId()))));

        for (int i = 0; i < savedGames.size(); i++) {
            Game savedGame = savedGames.get(i);
            String jsonPathPrefix = String.format("$[%d].", i);
            resultActions.andExpect(jsonPath(jsonPathPrefix + "id").value(savedGame.getId()))
                    .andExpect(jsonPath(jsonPathPrefix + "name").value(savedGame.getName()));
        }
    }

    @Test
    void shouldCreateGamesWithUserRightCreds() throws Exception {
        ResultActions resultActions = mockMvc.perform(post(API_GAME_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(GAMES_JSON));

        for (int i = 0; i < games.size(); i++) {
            Game savedGame = games.get(i);
            String jsonPathPrefix = String.format("$[%d].", i);
            resultActions.andExpect(jsonPath(jsonPathPrefix + "name").value(savedGame.getName()))
                    .andExpect(jsonPath(jsonPathPrefix + "price").value(savedGame.getPrice()));
        }
    }
}

