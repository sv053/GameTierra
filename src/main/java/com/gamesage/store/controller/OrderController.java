package com.gamesage.store.controller;

import com.gamesage.store.domain.model.Order;
import com.gamesage.store.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public Order findOrderById(@PathVariable Integer id) {
        return orderService.findById(id);
    }

    @GetMapping
    public List<Order> findAllOrders() {
        return orderService.findAll();
    }

    @PostMapping("/{gameId}/{userId}")
    public String buyGame(@PathVariable Integer gameId, @PathVariable Integer userId) {
        return orderService.buyGame(gameId, userId).toString();
    }
}

