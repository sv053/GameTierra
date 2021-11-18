package com.gamesage.store.service;

import com.gamesage.store.domain.data.sample.SampleData;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.GameRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceIntegrationTest {

    @Test
    void buyGame_Success_BalanceUpdated() {
        GameRepository repository = new GameRepository();
        Game game = new Game(null, "mistery island", BigDecimal.ONE);
        List<Game> games = List.of(game);
        repository.createAll(games);
        GameService gameService = new GameService(repository);

        BigDecimal initBalance = game.getPrice();
        User user = new User(1, null, SampleData.TIERS.get(1), initBalance);

        gameService.buyGame(game.getId(), user);

        BigDecimal cashback = game.getPrice().multiply(BigDecimal.valueOf
                (user.getTier().getCashbackPercentage()));
        BigDecimal expectedBalance = initBalance.subtract(game.getPrice()).add(cashback)
                .setScale(2, RoundingMode.HALF_UP);

        assertEquals(expectedBalance, user.getBalance());
    }

    @Test
    void buyGame_Fail_PriceIsHigherThanBalance_BalanceUnchanged() {
        GameRepository repository = new GameRepository();
        Game game = new Game(null, BigDecimal.ONE);
        repository.createAll(List.of(game));
        GameService gameService = new GameService(repository);

        BigDecimal initBalance = BigDecimal.ZERO;
        User user = new User(1, "lark", SampleData.TIERS.get(1), initBalance);

        gameService.buyGame(game.getId(), user);

        assertEquals(initBalance, user.getBalance());
    }

    @Test
    void buyGame_Success_ReturnsTrue() {
        GameRepository repository = new GameRepository();
        Game game = SampleData.GAMES.get(0);
        repository.createAll(List.of(game));
        GameService gameService = new GameService(repository);

        User user = new User(1, "", SampleData.TIERS.get(1), game.getPrice());

        assertTrue(gameService.buyGame(game.getId(), user));
    }

    @Test
    void buyGame_Fail_CannotBuyAlreadyOwned_ReturnsFalse() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);

        Game game = repository.getAll().get(0);

        User user = new User(1, "", SampleData.TIERS.get(1), game.getPrice());
        user.addGame(game);

        assertFalse(gameService.buyGame(game.getId(), user));
    }

    @Test
    void findById_Fail_TheGameIsNotFound_Exception() {
        GameRepository repository = new GameRepository();
        GameService gameService = new GameService(repository);
        assertThrows(IllegalArgumentException.class, () -> gameService.findById(1213313));
    }

    @Test
    void findById_Success_TheRightGameIsFound() {
        GameRepository repository = new GameRepository();
        Game gameToSearch = new Game(null, "mistery island", BigDecimal.ONE);
        repository.createAll(List.of(gameToSearch));
        GameService gameService = new GameService(repository);

        assertEquals(gameToSearch, gameService.findById(gameToSearch.getId()));
    }
}

