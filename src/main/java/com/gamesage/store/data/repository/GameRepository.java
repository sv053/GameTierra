package com.gamesage.store.data.repository;

import com.gamesage.store.data.model.Game;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GameRepository implements DefaultRepository<Map<Integer, Game>, Game> {

    @Override
    public Map<Integer, Game> read(List<Game> items) {
        return items.stream()
                .collect(
                        Collectors.toMap(Game::getId, Function.identity(),
                                (oldValue, newValue) -> (newValue)));
    }
}

