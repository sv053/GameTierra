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
        User userToCreate = new User(null, "aqua", "marina", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
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
        LocalDateTime beforeDateTime = LocalDateTime.now();
        PurchaseIntent expectedPurchase =
                new PurchaseIntent
                        .Builder(game)
                        .gameIsBought(true)
                        .buyer(user)
                        .message(PurchaseIntent.PurchaseMessage.PURCHASE_SUCCESSFUL)
                        .orderDateTime(beforeDateTime)
                        .build();
        PurchaseIntent result = orderService.buyGame(game.getId(), user.getId());

        assertAll(
                () -> assertTrue(result.isBought()),
                () -> assertEquals(expectedPurchase.getBuyer(), result.getBuyer()),
                () -> assertEquals(expectedPurchase.getTargetGame(), result.getTargetGame()),
                () -> assertEquals(expectedPurchase.getMessage(), result.getMessage()),
                () -> assertBetweenTimePoints(beforeDateTime, result.getOrderDateTime())
        );
    }

    @Test
    void buyGame_gameIsBought_Success() {
        PurchaseIntent result = orderService.buyGame(game.getId(), user.getId());

        assertTrue(result.isBought());
    }

    @Test
    void buyGame_targetGameIsCorrect_Success() {
        PurchaseIntent expectedPurchase =
                new PurchaseIntent
                        .Builder(game)
                        .gameIsBought(true)
                        .buyer(user)
                        .message(PurchaseIntent.PurchaseMessage.PURCHASE_SUCCESSFUL)
                        .orderDateTime(LocalDateTime.now())
                        .build();
        PurchaseIntent result = orderService.buyGame(game.getId(), user.getId());

        assertEquals(expectedPurchase.getTargetGame(), result.getTargetGame());
    }

    @Test
    void buyGame_BuyerIsCorrect_Success() {
        PurchaseIntent expectedPurchase =
                new PurchaseIntent
                        .Builder(game)
                        .gameIsBought(true)
                        .buyer(user)
                        .message(PurchaseIntent.PurchaseMessage.PURCHASE_SUCCESSFUL)
                        .orderDateTime(LocalDateTime.now())
                        .build();
        PurchaseIntent result = orderService.buyGame(game.getId(), user.getId());

        assertEquals(expectedPurchase.getBuyer(), result.getBuyer());
    }

    @Test
    void buyGame_PurchaseTimeIsOk_Success() {
        LocalDateTime beforeDateTime = LocalDateTime.now();
        PurchaseIntent result = orderService.buyGame(game.getId(), user.getId());

        assertBetweenTimePoints(beforeDateTime, result.getOrderDateTime());
    }

    void assertBetweenTimePoints(LocalDateTime firstDateTime, LocalDateTime dateTime) {
        assertTrue(firstDateTime.isBefore(dateTime) && dateTime.isBefore(LocalDateTime.now()));
    }
}

