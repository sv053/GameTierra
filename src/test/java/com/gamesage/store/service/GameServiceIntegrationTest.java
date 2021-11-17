package com.gamesage.store.service;

import com.gamesage.store.domain.data.sample.SampleData;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.GameRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceIntegrationTest {

    @Test
    void buyGame_Success_CheckingCorrectComputingOfTheNewBalance() {
        GameRepository repository = new GameRepository();
        Game game = new Game(null, "mistery island", BigDecimal.ONE);
        List<Game> games = new ArrayList<>();
        games.add(game);
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
    void buyGame_Fail_WhenPriceIsHigherThanBalance_CheckIfTheBalanceIsTheSameAsBeforeTheIntent() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);

        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(1, "lark", SampleData.TIERS.get(1), initBalance);

        Optional<Game> findGameResult = repository.getAll().stream()
                .filter(g -> g.getPrice().compareTo(initBalance) > 0).findFirst();
        if (findGameResult.isPresent()) {
            gameService.buyGame(findGameResult.get().getId(), user);
        }
        assertEquals(initBalance, user.getBalance());
    }

    @Test
    void buyGame_Success_MethodReturnsTrue() {
        BigDecimal initBalance = BigDecimal.valueOf(156.82);
        User user = new User(1, "", SampleData.TIERS.get(1), initBalance);

        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);
        Optional<Game> game = repository.getAll().stream().findFirst();

        if (game.isPresent()) {
            assertTrue(gameService.buyGame(game.get().getId(), user));
        }
    }

    @Test
    void buyGame_Fail_CannotBuyAlreadyOwned_TheMethodReturnsFalse() {
        GameRepository repository = new GameRepository();
        repository.createAll(SampleData.GAMES);
        GameService gameService = new GameService(repository);
        int gameId = repository.getAll().size();
        Game game = repository.getAll().get(gameId - 1);

        User user = new User(1, "", SampleData.TIERS.get(1), game.getPrice());
        user.addGame(game);

        assertFalse(gameService.buyGame(gameId, user));
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
        List<Game> games = new ArrayList<>();
        games.add(gameToSearch);
        repository.createAll(games);
        GameService gameService = new GameService(repository);

        assertEquals(gameToSearch, gameService.findById(gameToSearch.getId()));
    }
}

