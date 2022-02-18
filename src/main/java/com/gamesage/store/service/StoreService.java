package com.gamesage.store.service;

import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Order;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.db.DbStoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class StoreService {

    private final DbStoreRepository storeRepository;
    private final UserService userService;
    private final GameService gameService;

    public StoreService(DbStoreRepository storeRepository,
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
    public boolean buyGame(int gameId, int userId) {
        User user = userService.findById(userId);
        Game game = gameService.findById(gameId);
        BigDecimal price = game.getPrice();
        if (user.canPay(price) && (!user.hasGame(game))) {
            BigDecimal cashback = calculateCashback(price, user);
            user.withdrawBalance(price);
            user.depositBalance(cashback);
            //    user.addGame(game);
            Order createdOrder = storeRepository.createOne(new Order(null, user, game, LocalDate.now()));
            User updatedUser = userService.updateBalance(user);

            if (updatedUser != null && createdOrder.getId() != null)
                return true;
        }
        return false;
    }
}

