package com.gamesage.store.service;

import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Rollback
class UserServiceDbIntegrationTest {

    @Autowired
    UserService userService;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void findById_Success() {

        List<User> users = userService.findAll();
        User user = users.get(0);

        assertEquals(user, userService.findById(user.getId()));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(
                jdbcTemplate, "user", "id = " + user.getId()));
    }

    @Test
    void findById_Failure() {

        List<User> users = userService.findAll();
        assertAll(
                () -> assertEquals(0, JdbcTestUtils.countRowsInTableWhere(
                        jdbcTemplate, "user", "id = 77777")),
                () -> assertThrows(EntityNotFoundException.class, () -> userService.findById(77777))
        );
    }

    @Test
    void findAll_Success() {

        assertEquals(JdbcTestUtils.countRowsInTable(jdbcTemplate, "user"), userService.findAll().size());
    }

    @Test
    void createUser_Success() {

        JdbcTestUtils.deleteFromTableWhere(jdbcTemplate, "user", "id = 99999999");
        User user = new User(99999999, "loco", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        userService.createOne(user);
        List<User> users = userService.findAll();
        assertTrue(users.contains(user));
    }

    @Test
    void createUser_Fail() {

        User user = new User(1, "anjana", new Tier(3, "SILVER", 10.0), BigDecimal.TEN);
        assertThrows(DuplicateKeyException.class, () -> userService.createOne(user));
    }
}

