package com.gamesage.store.service;


import com.gamesage.store.data.entity.Game;
import com.gamesage.store.data.entity.Order;
import com.gamesage.store.data.entity.User;
import com.gamesage.store.data.repository.SampleData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Store {
    private static Store instance;
    private final List<Game> games;
    private final Map<Integer, Game> gameById;
    private final List<Order> orders;

    private Store() {
        orders = new ArrayList<>();
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

    public BigDecimal calculateCashback(BigDecimal gamePrice, double userPercentage) {
        BigDecimal percentageShare = BigDecimal.valueOf(userPercentage * 0.01d);
        return gamePrice.multiply(percentageShare);
    }

    public boolean buyGame(int gameId, User user) {
        BigDecimal price = searchGame(gameId).getPrice();
        boolean canPay = Atm.canPay(user.getBalance(), price);
        boolean hasGame = user.getGames().contains(gameId);
        if (canPay && !hasGame) {
            double percentage = user.getTier().getCashbackPercentage();
            BigDecimal newBalance = pay(user.getBalance(), price, calculateCashback(price, percentage));
            user.updateBalance(newBalance);
            orders.add(new Order(new Date(), user.getId(), gameId));
            user.addGame(gameId);
            return true;
        }
        return false;
    }

    public BigDecimal pay(BigDecimal balance, BigDecimal price, BigDecimal cashback) {
        balance = Atm.withdrawBalance(balance, price);
        balance = Atm.depositBalance(balance, cashback);
        return balance;
    }
}

