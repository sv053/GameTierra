package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.service.GameService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

    private final Game firstGameToCreate = new Game("THE_LAST", BigDecimal.valueOf(73.28));
    private final Game secondGameToCreate = new Game("THE_LAST_OF_US", BigDecimal.valueOf(26.16));
    private final Game secondGame = new Game("THE_WITCHERS", BigDecimal.valueOf(112.28));
    private final Game firstGame = new Game("THE_WITCHER", BigDecimal.valueOf(118.21));
    private final List<Game> games = Arrays.asList(secondGame, firstGame);
    private final List<Game> gamesToCreate = Arrays.asList(firstGameToCreate, secondGameToCreate);


    private List<Game> savedGames;
    private Game savedGame;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private GameService gameService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setup() {
        savedGames = gameService.createAll(games);
        savedGame = savedGames.get(0);
    }

    @Test
    void shouldFindGameById() throws Exception {
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
        mockMvc.perform(get(API_GAME_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect((jsonPath("$.*.name", containsInAnyOrder(savedGames.get(0).getName(), savedGames.get(1).getName()))))
                .andExpect((jsonPath("$.*.id", containsInAnyOrder(savedGames.get(0).getId(),
                        savedGames.get(1).getId()))));
    }

    @Test
    void shouldCreateGamesWithUserRightCreds() throws Exception {
        String gamesJson = objectMapper.writeValueAsString(gamesToCreate);

        String responseContent =
                mockMvc.perform(post(API_GAME_ENDPOINT)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(gamesJson))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.*.name", containsInAnyOrder(firstGameToCreate.getName(), secondGameToCreate.getName())))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        assertTrue(responseContent.contains(firstGameToCreate.getPrice().toString()));
        assertTrue(responseContent.contains(secondGameToCreate.getPrice().toString()));
    }

    @AfterAll
    void tearDown() {
        gameService.deleteAll();
    }
}

