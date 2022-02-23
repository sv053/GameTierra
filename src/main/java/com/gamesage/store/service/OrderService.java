package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Order;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.db.DbOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    public PurchaseResult buyGame(int gameId, int userId) {
        User user = userService.findById(userId);
        PurchaseResult purchaseResult = new PurchaseResult();
        purchaseResult.lastGame = gameService.findById(gameId);

        BigDecimal price = purchaseResult.lastGame.getPrice();
        if (user.canPay(price)) {
            if (!user.hasGame(purchaseResult.lastGame)) {
                BigDecimal cashback = calculateCashback(price, user);
                user.withdrawBalance(price);
                user.depositBalance(cashback);
                storeRepository.createOne(new Order(null, user, purchaseResult.lastGame, LocalDateTime.now()));
                userService.updateBalance(user);
                purchaseResult.result = true;
                purchaseResult.message = " play now! ";
            } else {
                purchaseResult.message = " is already owned";
            }
        } else {
            purchaseResult.message = "the balance is " + user.getBalance()
                    + " and the game price is " + purchaseResult.lastGame.getPrice();
        }
        purchaseResult.user = userService.findById(userId);
        return purchaseResult;
    }

    public class PurchaseResult {

        public boolean result;
        public String message;
        public Game lastGame;
        public User user;
    }
}

