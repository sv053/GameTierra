package com.gamesage.store.service;

import com.gamesage.store.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceDbIntegrationTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;

    private User user;
    private Game game;

    @BeforeEach
    void init() {
        User userToCreate = new User(null, "aqua", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        user = userService.createOne(userToCreate);
        game = gameService.createOne(new Game("future in the past", BigDecimal.TEN));
    }

    @Test
    void findById_Success_TheOrderIsFound() {
        Order order = orderService.createNewOrder(user, game);
        orderService.saveOrder(order);
        List<Order> allOrdersInTheTable = orderService.findAll();

        Order orderToFind = allOrdersInTheTable.get(0);
        int orderId = orderToFind.getId();
        assertEquals(orderToFind, orderService.findById(orderId));
    }

    @Test
    void buyGame_Success() throws InterruptedException {
        PurchaseIntent result = orderService.buyGame(game.getId(), user.getId());
        Thread.sleep(5);
        Order order = orderService.createNewOrder(user, game);
        PurchaseIntent expectedResult =
                new PurchaseIntent
                        .Builder(game)
                        .gameIsBought(true)
                        .buyer(user)
                        .message(PurchaseIntent.Message.PURCHASE_SUCCESSFUL)
                        .orderDateTime(order.getDateTime())
                        .build();

        assertAll(
                () -> assertTrue(expectedResult.getOrderDateTime().isAfter(result.getOrderDateTime())),
                () -> assertTrue(expectedResult.getOrderDateTime().minusSeconds(1).isBefore(result.getOrderDateTime())),
                () -> assertTrue(result.isBought()),
                () -> assertEquals(expectedResult.getBuyer(), result.getBuyer()),
                () -> assertEquals(expectedResult.getTargetGame(), result.getTargetGame()),
                () -> assertEquals(expectedResult.getMessage(), result.getMessage())
        );
    }
}

