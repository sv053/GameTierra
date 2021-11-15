package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameRepository implements Repository<Game, Integer> {

    private final Map<Integer, Game> allGamesById;
    private final List<Game> games;
    private int gameIdCounter = 1;

    public GameRepository() {
        this.games = new ArrayList<>();
        this.allGamesById = new HashMap<>();
    }

    public List<Game> getGames() {
        return this.games;
    }

    @Override
    public List<Game> createAll(final List<Game> gamesToAdd) {
        final List<Game> gamesWithId = this.addIdToAll(gamesToAdd);
        this.games.addAll(gamesWithId);
        this.addGamesToMap(gamesWithId);
        return gamesWithId;
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
    public Optional<Game> findBy(final Integer key) {
        return Optional.ofNullable(this.allGamesById.get(key));
    }
}

