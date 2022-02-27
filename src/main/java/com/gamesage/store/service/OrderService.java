package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Order;
import com.gamesage.store.domain.model.PurchaseIntent;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final Repository<Order, Integer> repository;
    private final UserService userService;
    private final GameService gameService;

    public OrderService(Repository<Order, Integer> repository,
                        UserService userService, GameService gameService) {
        this.repository = repository;
        this.userService = userService;
        this.gameService = gameService;
    }

    public Order findById(int id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id));
    }

    public List<Order> findAll() {
        return repository.findAll();
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        BigDecimal percentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage());
        return gamePrice.multiply(percentage);
    }

    @Transactional
    public PurchaseIntent buyGame(int gameId, int userId) {
        Game game = gameService.findById(gameId);
        User user = userService.findById(userId);
        Order order = new Order(user, game, LocalDateTime.now());
        boolean isBought = false;
        PurchaseIntent.Message message = PurchaseIntent.Message.PLAY_NOW;

        BigDecimal price = game.getPrice();
        if (user.canPay(price)) {
            if (!user.hasGame(game)) {
                user.withdrawBalance(price);
                user.depositBalance(calculateCashback(price, user));
                order = repository.createOne(order);
                isBought = true;
                user = userService.updateBalance(user);
            } else {
                message = PurchaseIntent.Message.IS_ALREADY_OWNED;
            }
        } else {
            message = PurchaseIntent.Message.THE_GAME_PRICE_IS_HIGHER_THAN_THE_BALANCE;
        }
        return new PurchaseIntent.Builder(game)
                .gameIsBought(isBought)
                .buyer(user)
                .message(message)
                .orderDateTime(order.getDateTime())
                .build();
    }
}

