package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.sample.SampleData;
import com.gamesage.store.util.RandomBigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameRepositoryTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void toMap(int gameId) {

        Game game = new Game(1, "SKYRIM", null);
        List<Game> games = Arrays.asList(
                new Game(1, "GRAND_THEFT_AUTO", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                new Game(1, "SKYRIM", BigDecimal.valueOf(87.88d)),
                new Game(2, "ASSASSIN_S_CREED", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                new Game(2, "GRAND_THEFT_AUTO", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                new Game(3, "SKYRIM", BigDecimal.valueOf(87.88d)),
                new Game(3, "ASSASSIN_S_CREED", RandomBigDecimal.getAndFormatRandomBigDecimal())
        );

        GameRepository repository = new GameRepository().updateAll(games);
        assertNotNull(repository.getGameById().get(gameId));
        assertEquals(repository.getGameById().get(game.getId()), game);
        assertEquals(repository.getGameById().size(), 3);
    }

    @Test
    void findByIdNotFoundWrongKey() {
        GameRepository repository = new GameRepository().updateAll(SampleData.GAMES);
        assertThrows(IllegalArgumentException.class, () -> repository.findById(1213313313));
    }
}

