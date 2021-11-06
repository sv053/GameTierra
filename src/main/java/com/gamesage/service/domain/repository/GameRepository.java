package com.gamesage.service.domain.repository;

import com.gamesage.service.domain.model.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameRepository implements Repository<Game> {

    private Map<Integer, Game> gameById;
    private final List<Game> games;

    public GameRepository() {
        this.games = new ArrayList<>();
    }

    private void setGameId() {
        games.forEach(g -> g.setId(games.indexOf(g) + 1));
    }

    public List<Game> getGames() {
        return games;
    }

    @Override
    public List<Game> createAll(List<Game> items) {
        games.addAll(items);
        setGameId();
        return games;
    }

    private void getMapFromList() {
        gameById = games.stream()
                .collect(
                        Collectors.toMap(Game::getId, Function.identity(),
                                (oldValue, newValue) -> (newValue)));
    }

    public Game findById(int id) {
        getMapFromList();
        if (!gameById.containsKey(id))
            throw new IllegalArgumentException("Game id " + id + " does not exist");
        return gameById.get(id);
    }
}

