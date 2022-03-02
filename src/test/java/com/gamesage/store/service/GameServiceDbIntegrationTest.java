package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GameServiceDbIntegrationTest {

    @Autowired
    private GameService gameService;
    private Game game;

    @BeforeEach
    void init() {
        game = gameService.createOne(new Game("Test game", BigDecimal.TEN));
    }

    @Test
    void findById_Fail_TheGameIsNotFound_Exception() {
        assertThrows(EntityNotFoundException.class, () -> gameService.findById(1213313));
    }

    @Test
    void findById_Success_TheRightGameIsFound() {
        assertEquals(game, gameService.findById(game.getId()));
    }

    @Test
    void createAGame_Success() {
        assertAll(
                () -> assertTrue(gameService.findAll().contains(game)),
                () -> assertNotNull(game.getId()));
    }

    @Test
    void createGames_Success() {
        List<Game> games = List.of(
                new Game("Detroit: Become Humano", BigDecimal.TEN),
                new Game("Detroit: Become Androido", BigDecimal.TEN));

        List<Game> createdGames = gameService.createAll(games);
        List<Game> retrievedGames = gameService.findAll();
        assertTrue(retrievedGames.containsAll(createdGames));
        createdGames.forEach(g -> assertNotNull(g.getId()));
    }
}

