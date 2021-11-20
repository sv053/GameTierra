package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = GameRepository.class)
class GameRepositoryTest {

    @Test
    void findGame_NotFound() {
        GameRepository repository = new GameRepository();
        assertTrue(repository.findById(1213313313).isEmpty());
    }

    @Test
    void findGame_Success() {
        GameRepository repository = new GameRepository();
        List<Game> games = List.of(new Game("addedGame1", null), new Game("addedGame2", null));
        repository.createAll(games);
        Game game = games.get(0);
        Optional<Game> foundGame = repository.findById(game.getId());

        assertEquals(Optional.of(game), foundGame);
    }

    @Test
    void createAll() {
        GameRepository repository = new GameRepository();

        List<Game> games = List.of(new Game("addedGame1", null), new Game("addedGame2", null));
        repository.createAll(games);

        assertEquals(games.size(), repository.getAll().size());
        assertTrue(repository.getAll().containsAll(games));
    }
}

