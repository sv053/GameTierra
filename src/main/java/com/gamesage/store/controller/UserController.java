package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/findUser")
    public User findUserById(@RequestParam Integer id) {
        User user = null;
        if(userRepository.findById(id).isPresent()){
            user = userRepository.findById(id).get();
        }
        logger.info("User is found: " + user);
        return user;
    }

    @GetMapping("/findAllUsers")
    public List<User> findAllUsers() {
        List<User> users = userRepository.getAll();
        users.stream().forEach(u -> logger.info(u.toString()));
        return users;
    }

    @PostMapping("/createUser")
    public void createUser(@RequestBody User user){
        userRepository.createUser(user);
        logger.info("added" + user.toString());
    }
}

