package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.util.RandomBigDecimal;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GameRepositoryTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void read(int gameId) {

        Game game = new Game(1, "SKYRIM", null);
        List<Game> games = Arrays.asList(
                new Game(1, "GRAND_THEFT_AUTO", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                new Game(1, "SKYRIM", BigDecimal.valueOf(87.88d)),
                new Game(2, "ASSASSIN_S_CREED", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                new Game(2, "GRAND_THEFT_AUTO", RandomBigDecimal.getAndFormatRandomBigDecimal()),
                new Game(3, "SKYRIM", BigDecimal.valueOf(87.88d)),
                new Game(3, "ASSASSIN_S_CREED", RandomBigDecimal.getAndFormatRandomBigDecimal())
        );

        GameRepository repository = new GameRepository();
        Map<Integer, Game> gamesById = repository.read(games);

        assertNotNull(gamesById.get(gameId));
        assertEquals(gamesById.get(game.getId()), game);
        assertEquals(gamesById.size(), 3);
    }
}