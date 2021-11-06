package com.gamesage.service.service;


import com.gamesage.service.domain.model.Game;
import com.gamesage.service.domain.model.User;
import com.gamesage.service.domain.repository.GameRepository;
import com.gamesage.service.domain.sample.SampleData;

import java.math.BigDecimal;
import java.util.List;

public class Store {
    private final GameRepository repository;

    public Store(GameRepository rep) {
        repository = rep;
    }

    public List<Game> getGames() {
        return repository.createAll(SampleData.GAMES);
    }

    public Game searchGame(int id) {
        return repository.findById(id);
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        BigDecimal percentageShare = BigDecimal.valueOf(user.getTier().getCashbackPercentage() * 0.01d);
        return gamePrice.multiply(percentageShare);
    }

    public boolean buyGame(int gameId, User user) {
        Game game = searchGame(gameId);
        BigDecimal price = game.getPrice();
        if (user.canPay(price) && (!user.hasGame(game))) {
            BigDecimal cashback = calculateCashback(price, user);
            user.withdrawBalance(price);
            user.depositBalance(cashback);
            user.addGame(game);
        }
        return false;
    }
}

