package com.gamesage.store.service;


import com.gamesage.store.data.model.Game;
import com.gamesage.store.data.model.User;
import com.gamesage.store.data.repository.GameRepository;
import com.gamesage.store.data.sample.SampleData;

import java.math.BigDecimal;
import java.util.List;

public class Store {
    private static Store instance;
    private GameRepository repository;
    private Store() {
    }

    public static List<Game> getGames() {
        return SampleData.GAMES;
    }

    public static Store getInstance() {
        if (instance == null)
            instance = new Store();
        return instance;
    }

    public Game searchGame(int id) {
        repository = new GameRepository();
        Game game = repository.read(SampleData.GAMES).get(id);
        if (game == null) {
            throw new IllegalArgumentException("Game with id " + id + " not found");
        }
        return game;
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        BigDecimal percentageShare = BigDecimal.valueOf(user.getTier().getCashbackPercentage() * 0.01d);
        return gamePrice.multiply(percentageShare);
    }

    public boolean buyGame(int gameId, User user) {
        Game game = searchGame(gameId);
        BigDecimal price = game.getPrice();
        if (!user.getGames().contains(game)) {
            if (user.canPay(price)) {
                BigDecimal cashback = calculateCashback(price, user);
                user.withdrawBalance(price);
                user.depositBalance(cashback);
                user.addGame(game);
                return true;
            }
        }
        return false;
    }
}

