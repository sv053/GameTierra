package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@org.springframework.stereotype.Repository
public class GameRepository implements Repository<Game, Integer> {

    private final List<Game> games;
    private final Map<Integer, Game> allGamesById;
    private int gameIdCounter = 1;

    public GameRepository() {
        games = new ArrayList<>();
        allGamesById = new HashMap<>();
    }

    @Override
    public Optional<Game> findById(Integer key) {
        return Optional.ofNullable(allGamesById.get(key));
    }

    @Override
    public List<Game> findAll() {
        return games;
    }

    @Override
    public Game createOne(Game gameToAdd) {
        Game gameToAddWithId = assignGameId(gameToAdd);
        games.add(gameToAddWithId);
        allGamesById.put(gameToAddWithId.getId(), gameToAddWithId);
        return gameToAddWithId;
    }

    public List<Game> create(List<Game> gamesToAdd) {
        List<Game> gamesToAddWithId = addIdToAll(gamesToAdd);
        games.addAll(gamesToAddWithId);
        addGamesToMap(gamesToAddWithId);
        return gamesToAddWithId;
    }

    private List<Game> addIdToAll(List<Game> gamesToAddId) {
        gamesToAddId.forEach(this::assignGameId);
        return gamesToAddId;
    }

    private Game assignGameId(Game game) {
        game.setId(gameIdCounter++);
        return game;
    }

    private void addGamesToMap(List<Game> gamesToAdd) {
        Map<Integer, Game> mapForNewGames = gamesToAdd.stream()
                .collect(
                        Collectors.toMap(Game::getId, Function.identity(),
                                (oldValue, newValue) -> (newValue)));
        allGamesById.putAll(mapForNewGames);
    }
}

