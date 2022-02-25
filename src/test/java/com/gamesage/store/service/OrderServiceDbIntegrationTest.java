package com.gamesage.store.service;

import com.gamesage.store.domain.model.*;
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
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;

    @Test
    void findById_Success_TheOrderIsFound() {
        List<Order> allOrdersInTheTable = orderService.findAll();
        if (!allOrdersInTheTable.isEmpty()) {
            Order orderToFind = allOrdersInTheTable.get(0);
            int orderId = orderToFind.getId();
            assertEquals(orderToFind, orderService.findById(orderId));
        }
    }

    @Test
    void buyGame_Success() {
        User userToCreate = new User(null, "aqua", new Tier(
                3, null, 10.d), BigDecimal.TEN);
        User user = userService.createOne(userToCreate);
        Game game = gameService.createOne(new Game("future in the past", BigDecimal.TEN));
        PurchaseIntent expectedResult =
                new PurchaseIntent()
                        .status(true)
                        .targetGame(game)
                        .buyer(user);
        PurchaseIntent result = orderService.buyGame(game.getId(), user.getId());

        assertAll(
                () -> assertEquals(expectedResult.getStatus(), result.getStatus()),
                () -> assertEquals(expectedResult.getBuyer().getId(), result.getBuyer().getId()),
                () -> assertEquals(expectedResult.getTargetGame(), result.getTargetGame()),
                () -> assertEquals(expectedResult.getMessage(), result.getMessage()));
    }
}

