package com.gamesage.store.service;

import com.gamesage.store.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
        LocalDateTime dateTime = orderService.getLocalDateTime();
        Order order = orderService.createNewOrder(user, game, dateTime);
        orderService.saveOrder(order);
        List<Order> allOrdersInTheTable = orderService.findAll();

        Order orderToFind = allOrdersInTheTable.get(0);
        int orderId = orderToFind.getId();
        assertEquals(orderToFind, orderService.findById(orderId));
    }

    @Test
    void buyGame_Success() {
        LocalDateTime dateTime = orderService.getLocalDateTime();
        PurchaseIntent result = orderService.buyGame(game.getId(), user.getId(), dateTime);
        PurchaseIntent expectedResult =
                new PurchaseIntent
                        .Builder(game)
                        .gameIsBought(true)
                        .buyer(user)
                        .message(PurchaseIntent.Message.PURCHASE_SUCCESSFUL.getPhrase())
                        .orderDateTime(dateTime)
                        .build();

        assertEquals(expectedResult, result);
    }
}

