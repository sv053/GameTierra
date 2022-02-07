package com.gamesage.store.service;

import com.gamesage.store.domain.data.SampleData;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.GameRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {GameRepository.class})
class GameServiceIntegrationTest {

    @Autowired
    GameRepository repository;

    @Test
    void buyGame_Success_BalanceUpdated() {
        Game game = new Game("mistery island", BigDecimal.ONE);
        List<Game> games = List.of(game);
        repository.create(games);
        GameService gameService = new GameService(repository);

        BigDecimal initBalance = game.getPrice();
        User user = new User(1, "", SampleData.TIERS.get(1), initBalance);

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
        Game game = new Game(null, BigDecimal.ONE);
        repository.create(List.of(game));
        GameService gameService = new GameService(repository);

        BigDecimal initBalance = BigDecimal.ZERO;
        User user = new User(1, "lark", SampleData.TIERS.get(1), initBalance);

        gameService.buyGame(game.getId(), user);

        assertEquals(initBalance, user.getBalance());
    }

    @Test
    void buyGame_Success_ReturnsTrue() {
        Game game = SampleData.GAMES.get(0);
        repository.create(List.of(game));
        GameService gameService = new GameService(repository);

        User user = new User(1, "", SampleData.TIERS.get(1), game.getPrice());

        assertTrue(gameService.buyGame(game.getId(), user));
    }

    @Test
    void buyGame_Fail_CannotBuyAlreadyOwned_ReturnsFalse() {
        repository.create(SampleData.GAMES);
        GameService gameService = new GameService(repository);

        Game game = repository.findAll().get(0);

        User user = new User(1, "", SampleData.TIERS.get(1), game.getPrice());
        user.addGame(game);

        assertFalse(gameService.buyGame(game.getId(), user));
    }

    @Test
    void findById_Fail_TheGameIsNotFound_Exception() {
        GameService gameService = new GameService(repository);
        assertThrows(SQLException.class, () -> gameService.findById(1213313));
    }

    @Test
    void findById_Success_TheRightGameIsFound() {
        Game gameToSearch = new Game("mistery island", BigDecimal.ONE);
        repository.create(List.of(gameToSearch));
        GameService gameService = new GameService(repository);

        try {
            assertEquals(gameToSearch, gameService.findById(gameToSearch.getId()));
        } catch (SQLException e) {
        }
    }
}