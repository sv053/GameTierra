package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameRepository implements Repository<Game>, FindById<Game> {

    private final Map<Integer, Game> allGamesById;
    private final List<Game> games;
    private int gameIdCounter = 1;

    public GameRepository() {
        games = new ArrayList<>();
        allGamesById = new HashMap<>();
    }

    public List<Game> getGames() {
        return games;
    }

    @Override
    public Game findBy(Integer id) {
        return allGamesById.containsKey(id) ? allGamesById.get(id) : null;
    }

    @Override
    public List<Game> createAll(List<Game> gamesToAdd) {
        List<Game> gamesWithId = addIdToAll(gamesToAdd);
        games.addAll(gamesWithId);
        addGamesToMapFromList(gamesWithId);
        return gamesWithId;
    }

    @Override
    public Optional<Game> findBy(final Optional<Integer> key) {
        return Optional.empty();
    }

    private Map<Integer, Game> addGamesToMapFromList(List<Game> gamesToAdd) {
        Map<Integer, Game> mapForNewGames = gamesToAdd.stream()
                .collect(
                        Collectors.toMap(Game::getId, Function.identity(),
                                (oldValue, newValue) -> (newValue)));
        allGamesById.putAll(mapForNewGames);
        return allGamesById;
    }

    private List<Game> addIdToAll(List<Game> gamesToAddId) {
        gamesToAddId.forEach(this::setGameId);
        return gamesToAddId;
    }

    private void setGameId(Game game) {
        game.setId(gameIdCounter++);
    }
}

