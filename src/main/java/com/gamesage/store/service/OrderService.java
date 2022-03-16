package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Order;
import com.gamesage.store.domain.model.PurchaseIntent;
import com.gamesage.store.domain.model.PurchaseIntent.Builder;
import com.gamesage.store.domain.model.PurchaseIntent.PurchaseMessage;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.Repository;
import com.gamesage.store.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Transactional
    public PurchaseIntent buyGame(int gameId, int userId) {
        Game game = gameService.findById(gameId);
        User user = userService.findById(userId);
        boolean canPay = user.canPay(game.getPrice());
        boolean hasGame = user.hasGame(game);
        boolean canBuy = canBuy(canPay, hasGame);
        PurchaseMessage purchaseMessage = preparePurchaseMessage(canPay, hasGame);
        Order order = null;

        if (canBuy) {
            pay(user, game);
            user = userService.updateBalance(user);
            order = repository.createOne(new Order(user, game));
        }
        LocalDateTime dateTime = (order == null) ? LocalDateTime.now() : order.getDateTime();
        return new Builder(game)
                .gameIsBought(canBuy)
                .buyer(user)
                .message(purchaseMessage)
                .orderDateTime(dateTime)
                .build();
    }

    private boolean canBuy(boolean canPay, boolean hasGame) {
        return canPay && !hasGame;
    }

    private PurchaseMessage preparePurchaseMessage(boolean canPay, boolean hasGame) {
        if (!canPay) return NOT_ENOUGH_BALANCE;
        if (hasGame) return ALREADY_OWNED;
        return PURCHASE_SUCCESSFUL;
    }

    public BigDecimal calculateCashback(BigDecimal gamePrice, User user) {
        BigDecimal percentage = BigDecimal.valueOf(user.getTier().getCashbackPercentage());
        return gamePrice.multiply(percentage);
    }

    private void pay(User user, Game game) {
        BigDecimal price = game.getPrice();
        user.withdrawBalance(price);
        user.depositBalance(calculateCashback(price, user));
    }
}

