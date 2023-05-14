package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.WrongCredentialsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TokenServiceIntegrationTest {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;

    @Test
    void findByTokenValue_Success() {
        User userWithoutToken = new User(null, "user1", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(userWithoutToken);
        AuthToken tokenToCreate = new AuthToken("ftyytgiuhiuhiuh", savedUser.getId());
        AuthToken tokenToFind = tokenService.createToken(tokenToCreate);
        AuthToken foundToken = tokenService.findToken(tokenToCreate.getValue());

        assertEquals(tokenToFind, foundToken);
    }

    @Test
    void findByLogin_Success() {
        User userWithoutToken = new User(null, "user111", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(userWithoutToken);
        AuthToken token = new AuthToken("ftyytgiuhiuhiuh", savedUser.getId());
        AuthToken tokenToFind = tokenService.createToken(token);
        Optional<AuthToken> foundToken = tokenService.findTokenById(savedUser.getId());

        assertTrue(foundToken.isPresent());
        assertEquals(Optional.of(tokenToFind), foundToken);
    }

    @Test
    void findByLogin_Failure_Exception() {
        assertThrows(WrongCredentialsException.class, () -> tokenService.findToken("cfgjgvuikhyvbfbu"));
    }

    @Test
    void saveTokenValue_Success() {
        User user = new User(null, "agamer", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(user);
        AuthToken token = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId());
        tokenService.createToken(token);
        Optional<AuthToken> foundToken = tokenService.findTokenById(savedUser.getId());

        assertEquals(Optional.of(token), foundToken);
    }
}

