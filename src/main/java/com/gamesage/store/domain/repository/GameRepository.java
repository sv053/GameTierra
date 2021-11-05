package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameRepository implements Repository<Game> {

    private Map<Integer, Game> gameById;
    private List<Game> games;

    // for test only
    public Map<Integer, Game> getGameById() {
        toMap();
        return gameById;
    }

    @Override
    public List<Game> findAll() {
        return games;
    }

    @Override
    public GameRepository updateAll(List<Game> items) {
        games = items;
        return this;
    }

    private void toMap() {
        gameById = games.stream()
                .collect(
                        Collectors.toMap(Game::getId, Function.identity(),
                                (oldValue, newValue) -> (newValue)));
    }

    @Override
    public Game findById(int id) {
        if (gameById == null)
            toMap();
        if (!gameById.containsKey(id))
            throw new IllegalArgumentException("Game id " + id + "does not exist");
        Game game = gameById.get(id);
        return game;
    }
}

