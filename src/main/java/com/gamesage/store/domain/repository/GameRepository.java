package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@org.springframework.stereotype.Repository
public class GameRepository implements Repository<Game, Integer> {

    private final List<Game> games;
    private int gameIdCounter = 1;
    private final Map<Integer, Game> allGamesById;

    public GameRepository() {
        games = new ArrayList<>();
        allGamesById = new HashMap<>();
    }

    @Override
    public Optional<Game> findById(Integer key) {
        return Optional.ofNullable(allGamesById.get(key));
    }

    public List<Game> getAll() {
        return games;
    }

    @Override
    public void createAll(List<Game> gamesToAdd) {
        List<Game> gamesToAddWithId = addIdToAll(gamesToAdd);
        games.addAll(gamesToAddWithId);
        addGamesToMap(gamesToAddWithId);
    }

    private List<Game> addIdToAll(List<Game> gamesToAddId) {
        gamesToAddId.forEach(this::setGameId);
        return gamesToAddId;
    }

    private void setGameId(Game game) {
        game.setId(gameIdCounter++);
    }

    private void addGamesToMap(List<Game> gamesToAdd) {
        Map<Integer, Game> mapForNewGames = gamesToAdd.stream()
                .collect(
                        Collectors.toMap(Game::getId, Function.identity(),
                                (oldValue, newValue) -> (newValue)));
        allGamesById.putAll(mapForNewGames);
    }
}

