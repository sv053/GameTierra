package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.WrongCredentialsException;
import com.gamesage.store.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:application-test.properties")
class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @Test
    void authenticateUser_Success() {
        User user = new User(0, "user11111", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(user);
        assertNotNull(userService.findByLogin(user.getLogin()));

        AuthToken token = authService.authenticateUser(new User(
                0, "user11111", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN
        ));
        assertNotNull(token);
        assertNotNull(token.getValue());
        assertEquals(savedUser.getId(), token.getUserId());
    }

    @Test
    void authenticateUser_Exception() {
        User user = new User(null, "user1", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);

        assertThrows(WrongCredentialsException.class, () -> authService.authenticateUser(user));
    }
}

