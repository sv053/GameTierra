package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameRepository implements Repository<Game, Integer> {

    private final List<Game> games;
    private int gameIdCounter = 1;
    private final Map<Integer, Game> allGamesById;

    public GameRepository() {
        this.games = new ArrayList<>();
        this.allGamesById = new HashMap<>();
    }

    public List<Game> getAll() {
        return this.games;
    }

    @Override
    public void createAll(final List<Game> gamesToAdd) {
        final List<Game> gamesToAddWithId = this.addIdToAll(gamesToAdd);
        this.games.addAll(gamesToAddWithId);
        this.addGamesToMap(gamesToAddWithId);
    }

    private List<Game> addIdToAll(final List<Game> gamesToAddId) {
        gamesToAddId.forEach(this::setGameId);
        return gamesToAddId;
    }

    private void setGameId(final Game game) {
        game.setId(this.gameIdCounter++);
    }

    private Map<Integer, Game> addGamesToMap(final List<Game> gamesToAdd) {
        final Map<Integer, Game> mapForNewGames = gamesToAdd.stream()
                .collect(
                        Collectors.toMap(Game::getId, Function.identity(),
                                (oldValue, newValue) -> (newValue)));
        this.allGamesById.putAll(mapForNewGames);
        return this.allGamesById;
    }

    @Override
    public Optional<Game> findById(Integer key) {
        return Optional.ofNullable(this.allGamesById.get(key));
    }
}

