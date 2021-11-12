package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameRepository implements Repository<Game>, FindById<Game> {

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
    public Game findBy(final Integer id) {
        return this.allGamesById.containsKey(id) ? this.allGamesById.get(id) : null;
    }

    @Override
    public List<Game> createAll(final List<Game> gamesToAdd) {
        final List<Game> gamesWithId = this.addIdToAll(gamesToAdd);
        this.games.addAll(gamesWithId);
        this.addGamesToMapFromList(gamesWithId);
        return gamesWithId;
    }

    private Map<Integer, Game> addGamesToMapFromList(final List<Game> gamesToAdd) {
        final Map<Integer, Game> mapForNewGames = gamesToAdd.stream()
                .collect(
                        Collectors.toMap(Game::getId, Function.identity(),
                                (oldValue, newValue) -> (newValue)));
        this.allGamesById.putAll(mapForNewGames);
        return this.allGamesById;
    }

    private List<Game> addIdToAll(final List<Game> gamesToAddId) {
        gamesToAddId.forEach(this::setGameId);
        return gamesToAddId;
    }

    private void setGameId(final Game game) {
        game.setId(this.gameIdCounter++);
    }
}

