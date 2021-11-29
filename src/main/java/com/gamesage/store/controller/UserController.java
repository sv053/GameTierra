package com.gamesage.store.controller;

import com.gamesage.store.domain.data.sample.SampleData;
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
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/findUser")
    public User findUserById(@RequestParam Integer id) {
        userService.findAll(SampleData.USERS);
        User user = userService.findById(id);
        logger.info("User is found: " + user);
        return user;
    }

    @GetMapping("/createUsers")
    public List<User> createUsers(){
        List<User> users = userService.findAll(SampleData.USERS);
        users.stream().forEach(u -> logger.info(u.toString()));
        return userService.findAll(SampleData.USERS);
    }
}

