package com.gamesage.store.controller;

import com.gamesage.store.domain.model.Card;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
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

    @PostMapping("/{id}/{cardNumber}/{cardholder}/{date}/{cvc}/{amount}")
    public User topUp(@RequestBody Integer id, @RequestBody Long cardNumber,
                      @RequestBody LocalDateTime date, @RequestBody String cardholder,
                      @RequestBody Integer cvc, @RequestBody BigDecimal amount) {
        boolean paid = false;
        User user = null;
        Card card = new Card(cardNumber, cardholder, date, cvc, amount);
        // third party api checks the card, helps to pay and returns bool
        // paid = true/false;
        if (paid) {
            user = userService.updateBalance(userService.findById(id));
        }
        return user;
    }
}

