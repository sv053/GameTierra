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
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/read/{id}")
    public User findUserById(@PathVariable Integer id) {
        User user = userService.findById(id);
        UserController.logger.info("User: " + user);
        return user;
    }

    @GetMapping("/read")
    public List<User> findAllUsers() {
        List<User> users = userService.findAll();
        UserController.logger.info("There are " + users.size() + " users");
        return users;
    }

    @PostMapping("/createOne")
    public User createUser(@RequestBody User user){
        userService.createAll(List.of(user));
        UserController.logger.info(user + " was added");
        return user;
    }
}

