package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Order;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.db.DbOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class OrderService {

    private final DbOrderRepository storeRepository;
    private final UserService userService;
    private final GameService gameService;

    public OrderService(DbOrderRepository storeRepository,
                        UserService userService, GameService gameService) {
        this.storeRepository = storeRepository;
        this.userService = userService;
        this.gameService = gameService;
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        BigDecimal percentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage());
        return gamePrice.multiply(percentage);
    }

    @Transactional
    public Map<Boolean, User> buyGame(int gameId, int userId) {
        User user = userService.findById(userId);
        Game game = gameService.findById(gameId);
        User updatedUser = null;

        BigDecimal price = game.getPrice();
        if (user.canPay(price) && (!user.hasGame(game))) {
            BigDecimal cashback = calculateCashback(price, user);
            user.withdrawBalance(price);
            user.depositBalance(cashback);
            storeRepository.createOne(new Order(null, user, game, LocalDateTime.now()));
            updatedUser = userService.updateBalance(user);
        }
        return Map.of((updatedUser != null), userService.findById(userId));
    }
}

