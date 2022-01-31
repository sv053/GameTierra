package com.gamesage.store.service;

import com.gamesage.store.GameTierra;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.db.DbGameRepository;
import com.gamesage.store.domain.repository.db.DbUserRepository;
import com.gamesage.store.exception.EmptyResultDataAccessException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes= {DbUserRepository.class, GameTierra.class})
class UserServiceDbIntegrationTest {

    @Autowired
    UserService userService;

    @Test
    void findById_Success() {

        List<User> users = userService.findAll();
        User user = users.get(0);

        assertEquals(user, userService.findById(user.getId()));
    }

    @Test
    void findById_Failure() {

        List<User> users = userService.findAll();
        assertAll(
                ()-> assertEquals(false, users.contains(userService.findById(-789))),
                ()-> assertThrows(EmptyResultDataAccessException.class, () -> userService.findById(1213313))
        );
    }

    @Test
    void findAll_Success() {

        assertTrue(userService.findAll().size() > 0);
    }

    @Test
    void findAll_Failure() {

        List<User> users = userService.findAll();
        assertAll(
                ()-> assertTrue(users.isEmpty()),
                ()-> assertThrows(EmptyResultDataAccessException.class, () -> userService.findAll())
        );
    }

    @Test
    void createUser_Success() {

        User user = new User(new Random().nextInt(), "loco", new Tier(3, "SILVER", 10.d), BigDecimal.TEN );
        userService.createOne(user);
        List<User> users = userService.findAll();
        assertTrue(users.contains(user));
    }

//    @Test
//    void createUser_Fail() {
//
//        User user = new User(new Random().nextInt(), "loco", new Tier(3, "SILVER", 10.d), BigDecimal.TEN );
//        userService.createOne(user);
//        List<User> users = userService.findAll();
//        assertFalse(users.contains(user));    }
}

