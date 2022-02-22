package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.OrderService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{gameId}/{userId}")
    public Map<Boolean, User> buyGame(@PathVariable Integer gameId, @PathVariable Integer userId) {
        return orderService.buyGame(gameId, userId);
    }
}

