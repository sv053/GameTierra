package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;
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
        User savedUser;
        try {
            savedUser = userService.createOne(user);
        } catch (SQLException e) {
            throw new ResponseStatusException(
                    HttpStatus.PRECONDITION_FAILED, "user was not added", e);
        }
        return savedUser;
    }
}

