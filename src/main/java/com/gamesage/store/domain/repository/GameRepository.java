package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Order;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class GameRepository implements CreateManyAndBuyOneRepository<Game, Integer> {

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

    @Override
    public int update(Game item) {
        return 0;
    }

    @Override
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

    @Override
    public Order createOrder(Order orderToAdd) {
        return null;
    }
}

