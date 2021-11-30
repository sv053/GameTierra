package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
        User user = userService.findById(id);
        return user;
    }

    @GetMapping("/")
    public List<User> findAllUsers() {
        List<User> users = userService.findAll();
        return users;
    }

    @PostMapping("/userslist")
    public List<User> createUsers(@RequestBody List<User> users){
        userService.create(users);
        return users;
    }

    @PostMapping("/")
    public User createOne(@RequestBody User user){
        userService.createOne(user);
        return user;
    }
}

