package com.gamesage.store.service;


import com.gamesage.store.data.entity.Game;
import com.gamesage.store.data.entity.User;
import com.gamesage.store.data.repository.SampleData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Store {
    private static Store instance;
    private final List<Game> games;
    private final Map<Integer, Game> gameById;

    private Store() {
        games = SampleData.GAMES;
        gameById = games.stream()
                .collect(
                        Collectors.toMap(Game::getId, Function.identity(),
                                (oldValue, newValue) -> (oldValue)));
    }

    public static Store getInstance() {
        if (instance == null)
            instance = new Store();
        return instance;
    }

    public Game searchGame(int id) {
        Game game = gameById.get(id);
        if (game == null) {
            throw new IllegalArgumentException("Game with id " + id + " not found");
        }
        return game;
    }

    public List<Game> getAllGames() {
        return games;
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        double percentage = user.getTier().getCashbackPercentage();
        BigDecimal percentageShare = BigDecimal.valueOf(percentage * 0.01d);
        return gamePrice.multiply(percentageShare);
    }

    public boolean buyGame(int gameId, User user) {
        Game gameToBuy = searchGame(gameId);
        BigDecimal price = gameToBuy.getPrice();
        if (user.canPay(price) && !user.hasGame(gameToBuy)) {
            BigDecimal cashback = calculateCashback(price, user);
            user.withdrawBalance(price);
            user.depositBalance(cashback);
            user.addGame(gameToBuy);
            return true;
        }
        return false;
    }
}

