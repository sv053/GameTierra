package com.gamesage.store.service;

import com.gamesage.store.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class OrderServiceDbIntegrationTest {
    User userToCreate;
    User user;
    Game game;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;

    @BeforeEach
    void init() {
        userToCreate = new User(null, "aqua", new Tier(
                3, null, 10.d), BigDecimal.TEN);
        user = userService.createOne(userToCreate);
        game = gameService.createOne(new Game("future in the past", BigDecimal.TEN));
    }

    @Test
    void findById_Success_TheOrderIsFound() {
        orderService.buyGame(game.getId(), user.getId());
        List<Order> allOrdersInTheTable = orderService.findAll();

        Order orderToFind = allOrdersInTheTable.get(0);
        int orderId = orderToFind.getId();
        assertEquals(orderToFind, orderService.findById(orderId));
    }

    @Test
    void buyGame_Success() {
        PurchaseIntent expectedResult =
                new PurchaseIntent
                        .Builder(game)
                        .gameIsBought(true)
                        .buyer(user)
                        .message("play now!")
                        .build();
        PurchaseIntent result = orderService.buyGame(game.getId(), user.getId());

        assertAll(
                () -> assertEquals(expectedResult.isBought(), result.isBought()),
                () -> assertEquals(expectedResult.getBuyer().getId(), result.getBuyer().getId()),
                () -> assertEquals(expectedResult.getTargetGame(), result.getTargetGame()),
                () -> assertEquals(expectedResult.getMessage(), result.getMessage()));
    }
}

