package com.gamesage.store.domain.repository;

import com.gamesage.store.domain.model.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameRepository implements Repository<Game> {

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

    private void setGameId(Game game) {
        game.setId(gameIdCounter++);
    }

    private List<Game> setIdToAll(List<Game> gamesToAddId) {
        gamesToAddId.forEach(g -> setGameId(g));
        return gamesToAddId;
    }

    @Override
    public List<Game> createAll(List<Game> gamesToAdd) {
        games.addAll(setIdToAll(gamesToAdd));
        addGamesToMapFromList(gamesToAdd);
        return games;
    }

    private Map<Integer, Game> addGamesToMapFromList(List<Game> gamesToAdd) {
        Map mapForNewGames = gamesToAdd.stream()
                .collect(
                        Collectors.toMap(Game::getId, Function.identity(),
                                (oldValue, newValue) -> (newValue)));
        allGamesById.putAll(mapForNewGames);
        return allGamesById;
    }

    @Override
    public Game findBy(int id) {
        if (!(allGamesById == null || allGamesById.isEmpty())
                && allGamesById.containsKey(id))
            return allGamesById.get(id);
        else return null;
    }
}

