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

import static com.gamesage.store.domain.model.PurchaseIntent.Message.*;

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

    private User retrieveUser(int id) {
        return userService.findById(id);
    }

    private Game retrieveGame(int id) {
        return gameService.findById(id);
    }

    private void pay(User user, Game game) {
        BigDecimal price = game.getPrice();
        user.withdrawBalance(price);
        user.depositBalance(calculateCashback(price, user));
    }

    private boolean ifCanBuy(boolean canPay, boolean hasGame) {
        return !hasGame && canPay;
    }

    private String createPurchaseMessage(boolean canPay, boolean hasGame) {
        if (!canPay) return THE_GAME_PRICE_IS_HIGHER_THAN_THE_BALANCE.getPhrase();
        if (hasGame) return IS_ALREADY_OWNED.getPhrase();
        return PURCHASE_SUCCESSFUL.getPhrase();
    }

    public LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }

    public Order createNewOrder(User user, Game game, LocalDateTime dateTime) {
        return new Order(user, game, dateTime);
    }

    public Order saveOrder(Order order) {
        return repository.createOne(order);
    }

    @Transactional
    public PurchaseIntent buyGame(int gameId, int userId, LocalDateTime dateTime) {
        Game game = retrieveGame(gameId);
        User user = retrieveUser(userId);
        boolean hasGame = user.hasGame(game);
        boolean canPay = user.canPay(game.getPrice());
        boolean canBuy = ifCanBuy(canPay, hasGame);
        String message = createPurchaseMessage(canPay, hasGame);
        Order order = createNewOrder(user, game, dateTime);
        saveOrder(order);
        if (canBuy) {
            pay(user, game);
            user = userService.updateBalance(user);
            order = repository.createOne(order);
        }
        return new PurchaseIntent.Builder(game)
                .gameIsBought(canBuy)
                .buyer(user)
                .message(message)
                .orderDateTime(order.getDateTime())
                .build();
    }
}

