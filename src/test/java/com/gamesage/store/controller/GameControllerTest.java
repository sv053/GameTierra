package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.exception.EntityNotFoundException;
import com.gamesage.store.service.GameService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("IntegrationTest")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "server.port=8084")
@TestPropertySource(locations = "classpath:application.properties")
@SpringJUnitConfig
@AutoConfigureMockMvc()
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;


    @Test
    public void shouldFindGameById() throws Exception {
        Game game = new Game(8, "THE_WITCHER", BigDecimal.valueOf(17.28d));

        given(gameService.findById(8)).willReturn(game);

        mockMvc.perform(get("/games/8"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(8))
                .andExpect(jsonPath("$.name").value(game.getName()))
                .andExpect(jsonPath("$.price").value(game.getPrice()));
    }

    @Test
    public void shouldThrowExceptionIfIdDoesNotExistForFindGameById() throws Exception {
        int id = 100;
        given(gameService.findById(anyInt())).willThrow(new EntityNotFoundException(id));

        mockMvc.perform(get(String.format("/games/%d", id))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldFindAllGames() throws Exception {
        Game game1 = new Game(8, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(9, "THE_LAST_OF_US", BigDecimal.ONE);

        List<Game> games = Arrays.asList(game1, game2);

        given(gameService.findAll()).willReturn(games);

        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(game1.getId()))
                .andExpect(jsonPath("$[0].name").value(game1.getName()))
                .andExpect(jsonPath("$[0].price").value(game1.getPrice()))
                .andExpect(jsonPath("$[1].id").value(game2.getId()))
                .andExpect(jsonPath("$[1].name").value(game2.getName()))
                .andExpect(jsonPath("$[1].price").value(game2.getPrice()));
    }

    @Test
    @WithMockUser(username = "admin", password = "letmein")
    public void shouldCreateGamesWithUserRightCreds() throws Exception {
        Game game1 = new Game(8, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(9, "THE_LAST_OF_US", BigDecimal.ONE);

        List<Game> gamesToSave = Arrays.asList(game1, game2);

        given(gameService.createAll(gamesToSave)).willReturn(gamesToSave);

        ObjectMapper mapper = new ObjectMapper();
        String gamesToSaveJson = mapper.writeValueAsString(gamesToSave);

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gamesToSaveJson)
                        .with(csrf())
                        .with(httpBasic("admin", "letmein")))
                .andExpect(jsonPath("$[0].id").value(game1.getId()))
                .andExpect(jsonPath("$[0].name").value(game1.getName()))
                .andExpect(jsonPath("$[0].price").value(game1.getPrice()))
                .andExpect(jsonPath("$[1].id").value(game2.getId()))
                .andExpect(jsonPath("$[1].name").value(game2.getName()))
                .andExpect(jsonPath("$[1].price").value(game2.getPrice()));
    }

    @Test
    public void shouldCreateGamesWithUserWrongCreds() throws Exception {
        Game game1 = new Game(8, "THE_WITCHER", BigDecimal.valueOf(17.28d));
        Game game2 = new Game(9, "THE_LAST_OF_US", BigDecimal.ONE);

        List<Game> gamesToSave = Arrays.asList(game1, game2);

        ObjectMapper mapper = new ObjectMapper();
        String gamesToSaveJson = mapper.writeValueAsString(gamesToSave);

        mockMvc.perform(post("/games")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gamesToSaveJson)
                        .with(csrf())
                        .with(httpBasic("nosuchuser", "wrongpassword")))
                .andExpect(status().isOk());

        verify(gameService, only()).createAll(anyList());
    }
}

