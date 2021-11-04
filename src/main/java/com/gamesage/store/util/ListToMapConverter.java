package com.gamesage.store.util;

import com.gamesage.store.data.model.Game;
import com.gamesage.store.data.sample.SampleData;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListToMapConverter {
    private final Map<Integer, Game> gameById;

    public ListToMapConverter() {
        gameById = SampleData.GAMES.stream()
                .collect(
                        Collectors.toMap(Game::getId, Function.identity(),
                                (oldValue, newValue) -> (oldValue)));
    }

    public Map<Integer, Game> getGameById() {
        return gameById;
    }
}

