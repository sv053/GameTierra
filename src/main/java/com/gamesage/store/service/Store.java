package com.gamesage.store.service;


import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;

import java.math.BigDecimal;
import java.util.List;

public class Store {
    private Repository repository;

    public void assignRepository(Repository rep) {
        this.repository = rep;
    }

    public List<Game> getGames() {
        return (List<Game>) repository.findAll();
    }

    public Game searchGame(int id) {
        return (Game) repository.findById(id);
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        BigDecimal percentageShare = BigDecimal.valueOf(user.getTier().getCashbackPercentage() * 0.01d);
        return gamePrice.multiply(percentageShare);
    }

    public boolean buyGame(int gameId, User user) {
        Game game = searchGame(gameId);
        BigDecimal price = game.getPrice();
        if (!user.hasGame(game)) {
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

