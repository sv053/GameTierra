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
        PurchaseIntent purchaseIntent = new PurchaseIntent()
                .status(false)
                .targetGame(game)
                .buyer(user);

        BigDecimal price = game.getPrice();
        if (user.canPay(price)) {
            if (!user.hasGame(game)) {
                user.withdrawBalance(price);
                user.depositBalance(calculateCashback(price, user));
                Order createdOrder = repository.createOne(new Order(user, game, LocalDateTime.now()));
                purchaseIntent.order(createdOrder)
                        .buyer(userService.updateBalance(user))
                        .status(true);
            } else {
                purchaseIntent.message("is already owned");
            }
        } else {
            purchaseIntent.message("the game price is higher than the balance");
        }
        userService.findById(userId);
        return purchaseIntent;
    }
}

