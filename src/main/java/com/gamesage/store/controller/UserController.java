package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.UserService;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@EnableWebMvc
@RestController
@RequestMapping("/users")
public class UserController{

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User findUserById (@PathVariable Integer id){
        return userService.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "User with id " + id + " is not found"));
    }

    @GetMapping
    public List<User> findAllUsers() {
        return userService.findAll();
    }

    @PostMapping
    public User createOne(@RequestBody User user) {
        return userService.createOne(user);
    }
}

