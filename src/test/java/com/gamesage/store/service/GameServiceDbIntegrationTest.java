package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class GameServiceDbIntegrationTest {

    @Autowired
    GameService gameService;

    @Test
    void buyGame_Success_BalanceUpdated() {

        Game game = gameService.findAll().get(0);

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

        Game game = gameService.findAll().get(0);

        BigDecimal initBalance = BigDecimal.ZERO;
        User user = new User(1, null, new Tier(null, null, 10d), initBalance);

        gameService.buyGame(game.getId(), user);

        assertEquals(initBalance, user.getBalance());
    }

    @Test
    void buyGame_Success_ReturnsTrue() {

        Game game = gameService.findAll().get(0);
        User user = new User(1, null, new Tier(null, null, 10d), game.getPrice());

        assertTrue(gameService.buyGame(game.getId(), user));
    }

    @Test
    void buyGame_Fail_CannotBuyAlreadyOwned_ReturnsFalse() {

        Game game = gameService.findAll().get(0);
        User user = new User(null, null, new Tier(null, null, 10d), game.getPrice());
        user.addGame(game);

        assertFalse(gameService.buyGame(game.getId(), user));
    }

    @Test
    void findById_Fail_TheGameIsNotFound_Exception() {

        assertThrows(EntityNotFoundException.class, () -> gameService.findById(1213313));
    }

    @Test
    void findById_Success_TheRightGameIsFound() {   // remove this?
        Game game = gameService.findAll().get(0);

        assertEquals(game, gameService.findById(game.getId())); // doesn't make any sense
    }
}

