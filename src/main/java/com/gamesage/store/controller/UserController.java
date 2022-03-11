package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.paymentapi.PaymentRequest;
import com.gamesage.store.paymentapi.PaymentResponse;
import com.gamesage.store.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
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
    public PaymentResponse tryTopUp(@Min(1) @NotNull @Valid @PathVariable Integer id, @Valid @RequestBody PaymentRequest paymentRequest) {
        return userService.updateUserIfPaymentSucceed(paymentRequest, id);
    }
}
// PaymentRequest example
//{
//        "publicId": "99ывоа8крииьодщ",
//        "amount": 10.0,
//        "card":{
//        "cardNumber": 4545984611112222,
//        "cardholderName": "errnumber",
//        "expireDate": "2025-12-12",
//        "cvcCode": 123
//        }
//        }

