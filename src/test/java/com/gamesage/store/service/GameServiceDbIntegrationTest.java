package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameServiceDbIntegrationTest {

    GameService gameService;
    JdbcTemplate jdbcTemplate;
    Game game;

    @Autowired
    public GameServiceDbIntegrationTest(GameService gameService, JdbcTemplate jdbcTemplate) {
        this.gameService = gameService;
        this.jdbcTemplate = jdbcTemplate;
        this.game = gameService.findAll().get(0);
    }

    @Test
    void buyGame_Success_BalanceUpdated() {
        BigDecimal initBalance = game.getPrice();
        User user = new User(null, "user1", new Tier(null, null, 10d), initBalance);

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
        assertThrows(SQLException.class, () -> gameService.findById(1213313));
    }

    @Test
    void findById_Success_TheRightGameIsFound() {
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "game", "id = 123");
        Game game = new Game(123, "Detroit: Become Human", BigDecimal.TEN);
        try {
            gameService.createAll(List.of(game));
            assertEquals(game, gameService.findById(game.getId()));
        } catch (SQLException e) {
            assertThrows(SQLException.class, () -> gameService.findById(123));
        }
    }

    @Test
    void createAGame_Success() {
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "game", "id = 123");
        Game game = new Game(123, "Detroit: Become Human", BigDecimal.TEN);
        try {
            gameService.createAll(List.of(game));
            assertTrue(gameService.findAll().contains(game));
        } catch (SQLException e) {
            assertThrows(SQLException.class, () -> gameService.createAll(List.of(game)));
        }

    }

    @Test
    void createGames_Success() {
        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "game", "id = 99998 OR id = 99999");
        List<Game> games = List.of(
                new Game(99998, "Detroit: Become Human", BigDecimal.TEN),
                new Game(99999, "Detroit: Become Android", BigDecimal.TEN));
        try {
            gameService.createAll(games);
            assertTrue(gameService.findAll().containsAll(games));
        } catch (SQLException e) {
            assertThrows(SQLException.class, () -> gameService.createAll(games));
        }
    }
}

