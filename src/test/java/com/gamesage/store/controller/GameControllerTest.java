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
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GameService gameService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFindGameById() throws Exception {
        Game game = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game createdGame = gameService.createOne(game);

        mockMvc.perform(get("/games/{gameId}", createdGame.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(createdGame.getId()))
                .andExpect(jsonPath("$.name").value(game.getName()))
                .andExpect(jsonPath("$.price").value(game.getPrice()));
    }

    @Test
    void shouldThrowException_whenTryToFindGameById_IdDoesNotExist() throws Exception {
        int id = -88888;

        mockMvc.perform(get(String.format("/games/%d", id))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFindAllGames() throws Exception {
        Game game1 = new Game(1, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(2, "THE_LAST_OF_US", BigDecimal.valueOf(7.28d));
        List<Game> games = Arrays.asList(game1, game2);
        List<Game> savedGames = gameService.createAll(games);

        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(savedGames.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(savedGames.get(0).getName()))
                .andExpect(jsonPath("$[0].price").value(savedGames.get(0).getPrice()))
                .andExpect(jsonPath("$[1].id").value(savedGames.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(savedGames.get(1).getName()))
                .andExpect(jsonPath("$[1].price").value(savedGames.get(1).getPrice()));
    }

    @Test
    void shouldCreateGamesWithUserRightCreds() throws Exception {
        Game game1 = new Game(null, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(null, "THE_LAST_OF_US", BigDecimal.ONE);

        List<Game> gamesToSave = Arrays.asList(game1, game2);

        String gamesToSaveJson = objectMapper.writeValueAsString(gamesToSave);

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gamesToSaveJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value(game1.getName()))
                .andExpect(jsonPath("$[0].price").value(game1.getPrice()))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value(game2.getName()))
                .andExpect(jsonPath("$[1].price").value(game2.getPrice()));
    }
}

