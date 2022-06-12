package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.paymentapi.PaymentRequest;
import com.gamesage.store.paymentapi.PaymentResponse;
import com.gamesage.store.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/login/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Integer id) {
        return userService.findById(id);
    }

    @GetMapping
    public List<User> findAllUsers() {
        return userService.findAll();
    }

    @PostMapping
    public User createOne(@RequestBody User user) {
        return userService.createOne(user);
    }

    @PostMapping("/{id}/topup")
    public PaymentResponse tryTopUp(@PathVariable int id, @Valid @RequestBody PaymentRequest paymentRequest) {
        return userService.topUpBalance(paymentRequest, id);
    }
}

