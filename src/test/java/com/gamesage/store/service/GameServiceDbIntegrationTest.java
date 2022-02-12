package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class GameServiceDbIntegrationTest {
    @Autowired
    GameService gameService;
    @Autowired
    JdbcTemplate jdbcTemplate;
    List<Game> games;
    Game game;

    @BeforeEach
    void init(){

        games = List.of(
                new Game( "Detroit: Become Human", BigDecimal.TEN),
                new Game( "Detroit: Become Android", BigDecimal.TEN));
        List<Game> gameList = gameService.createAll(games);
        game = gameList.get(0);
    }

    @Test
    void buyGame_Success_BalanceUpdated() {
        game = games.get(0);

        BigDecimal initBalance = game.getPrice();
        User user = new User(null, "bee", new Tier(null, null, 10d), initBalance);

        gameService.buyGame(game.getId(), user);

        BigDecimal cashback = game.getPrice()
                .multiply(BigDecimal.valueOf(user.getTier().getCashbackPercentage()));
        BigDecimal expectedBalance = initBalance
                .subtract(game.getPrice())
                .add(cashback)
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedBalance, user.getBalance());
    }

    @Test
    void buyGame_Fail_PriceIsHigherThanBalance_BalanceUnchanged() {
        BigDecimal initBalance = game.getPrice().subtract(BigDecimal.ONE);
        User user = new User(1, null, new Tier(null, null, 10d), initBalance);

        gameService.buyGame(game.getId(), user);

        assertEquals(initBalance, user.getBalance());
    }

    @Test
    void buyGame_Success_ReturnsTrue() {
        User user = new User(1, null, new Tier(null, null, 10d), game.getPrice());

        assertTrue(gameService.buyGame(game.getId(), user));
    }

    @Test
    void buyGame_Fail_CannotBuyAlreadyOwned_ReturnsFalse() {
        User user = new User(null, null, new Tier(null, null, 10d), game.getPrice());
        user.addGame(game);

        assertFalse(gameService.buyGame(game.getId(), user));
    }

    @Test
    void findById_Fail_TheGameIsNotFound_Exception() {
        assertThrows(EntityNotFoundException.class, () -> gameService.findById(1213313));
    }

    @Test
    void findById_Success_TheRightGameIsFound() {
        Game game = new Game( "Detroit: Become Human", BigDecimal.TEN);

        gameService.createOne(game);

        assertEquals(game, gameService.findById(game.getId()));
    }

    @Test
    void createAGame_Success() {
        Game gameToAdd = new Game( "Detroit: Become Human", BigDecimal.TEN);
        Game addedGame = gameService.createOne(gameToAdd);

        assertTrue(gameService.findAll().contains(addedGame));
    }

    @Test
    void createGames_Success() {
        List<Game> games = List.of(
                new Game( "Detroit: Become Human", BigDecimal.TEN),
                new Game( "Detroit: Become Android", BigDecimal.TEN));

        List<Game> newGames = gameService.createAll(games);

        assertTrue(gameService.findAll().containsAll(newGames));
    }
}

