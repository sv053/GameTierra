package com.gamesage.store.service;

import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceDbIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void findById_Success() {
        User user = new User(null, "aquamarina", new Tier(
                3, null, 10.d), BigDecimal.TEN);

        User userToFind = userService.createOne(user);
        User foundUser = userService.findById(userToFind.getId());

        assertEquals(userToFind, foundUser);
    }

    @Test
    void findById_Failure() {
        assertThrows(EntityNotFoundException.class, () -> userService.findById(77777));
    }

    @Test
    void findAll_Success() {
        List<User> usersToAdd = new ArrayList<>();
        usersToAdd.add(userService.createOne(new User(null, "primero", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN)));
        usersToAdd.add(userService.createOne(new User(null, "segundo", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN)));
        usersToAdd.add(userService.createOne(new User(null, "tercero", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN)));
        List<User> users = userService.findAll();

        assertTrue(users.containsAll(usersToAdd));
    }

    @Test
    void createUser_Success() {
        User user = new User(null, "loco", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User addedUser = userService.createOne(user);
        List<User> users = userService.findAll();
        assertAll(
                () -> assertTrue(users.contains(addedUser)),
                () -> assertNotNull(addedUser.getId()));
    }
}

