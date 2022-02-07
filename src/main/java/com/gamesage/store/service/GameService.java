package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.CreateManyRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@Service
public class GameService {

    private final CreateManyRepository<Game, Integer> repository;

    public GameService(@Qualifier("dbGameRepository") CreateManyRepository<Game, Integer> repository) {
        this.repository = repository;
    }

    public Game findById(int id) throws SQLException {
        return repository.findById(id).orElseThrow(SQLException::new);
    }

    public List<Game> findAll() {
        return repository.findAll();
    }

    public List<Game> createAll(List<Game> gamesToAdd) throws SQLException {
        return repository.create(gamesToAdd);
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        BigDecimal percentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage());
        return gamePrice.multiply(percentage);
    }

    public boolean buyGame(int gameId, User user) {
        Game game;
        try {
            game = findById(gameId);
        } catch (SQLException e) {
            return false;
        }
        BigDecimal price = game.getPrice();
        if (user.canPay(price) && (!user.hasGame(game))) {
            BigDecimal cashback = calculateCashback(price, user);
            user.withdrawBalance(price);
            user.depositBalance(cashback);
            user.addGame(game);
            return true;
        }
        return false;
    }
}

