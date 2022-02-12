package com.gamesage.store.service;

import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceDbIntegrationTest {

    @Autowired
    UserService userService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void findById_Success() {
        User user = new User(null,"aquamarina", new Tier(
                3, null, 10.d), BigDecimal.TEN);

        User userToFind = userService.createOne(user);
        User foundUser = userService.findById(userToFind.getId());

        assertEquals(userToFind, foundUser);
    }

    @Test
    void findById_Failure() {
        assertAll(() -> assertThrows(EntityNotFoundException.class, () -> userService.findById(77777)));
    }

    @Test
    void findAll_Success() {
        assertEquals(JdbcTestUtils.countRowsInTable(jdbcTemplate, "user")
                , userService.findAll().size());
    }

    @Test
    void createUser_Success() {
        User user = new User(null,"loco", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
            userService.createOne(user);
            List<User> users = userService.findAll();
            assertTrue(users.contains(user));
    }
}

