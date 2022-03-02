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
import java.util.List;

import static com.gamesage.store.domain.model.PurchaseIntent.PurchaseMessage.*;

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

    private boolean ifCanBuy(boolean canPay, boolean hasGame) {
        return !hasGame && canPay;
    }

    private PurchaseIntent.PurchaseMessage preparePurchaseMessage(boolean canPay, boolean hasGame) {
        if (!canPay) return NOT_ENOUGH_BALANCE;
        if (hasGame) return ALREADY_OWNED;
        return PURCHASE_SUCCESSFUL;
    }

    private void pay(User user, Game game) {
        BigDecimal price = game.getPrice();
        user.withdrawBalance(price);
        user.depositBalance(calculateCashback(price, user));
    }

    @Transactional
    public PurchaseIntent buyGame(int gameId, int userId) {
        Game game = gameService.findById(gameId);
        User user = userService.findById(userId);
        boolean hasGame = user.hasGame(game);
        boolean canPay = user.canPay(game.getPrice());
        boolean canBuy = ifCanBuy(canPay, hasGame);
        PurchaseIntent.PurchaseMessage purchaseMessage = preparePurchaseMessage(canPay, hasGame);
        Order order = new Order(user, game);

        if (canBuy) {
            pay(user, game);
            user = userService.updateBalance(user);
            order = repository.createOne(order);
        }
        return new PurchaseIntent.Builder(game)
                .gameIsBought(canBuy)
                .buyer(user)
                .message(purchaseMessage)
                .orderDateTime(order.getDateTime())
                .build();
    }
}

