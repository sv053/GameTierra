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
        game = gameService.createOne(new Game("Detroit: Become Human", BigDecimal.TEN));
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
        Game addedGame = gameService.createOne(game);

        assertAll(
                () -> assertTrue(gameService.findAll().contains(addedGame)),
                () -> assertNotNull(addedGame.getId()));
    }

    @Test
    void createGames_Success() {
        List<Game> games = List.of(
                new Game("Detroit: Become Human", BigDecimal.TEN),
                new Game("Detroit: Become Android", BigDecimal.TEN));

        List<Game> newGames = gameService.createAll(games);

        assertTrue(gameService.findAll().containsAll(newGames));
        newGames.forEach(g -> assertNotNull(g.getId()));
    }
}

