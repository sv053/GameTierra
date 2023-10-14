package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.WrongCredentialsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource("classpath:application-test.properties")
class TokenServiceIntegrationTest {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    void findByTokenValue_Success() {
        User userWithoutToken = new User(null, "user1", "lerida", new Tier(
            3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(userWithoutToken);
        AuthToken tokenToCreate = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now());
        AuthToken tokenToFind = tokenService.createToken(tokenToCreate);
        Optional<AuthToken> foundToken = tokenService.findTokenByUserId(tokenToFind.getUserId());
        String fountTokenValue = foundToken.get().getValue();
        assertNotNull(foundToken);
        assertTrue(encoder.matches(tokenToCreate.getValue(), fountTokenValue));
    }

    @Test
    void findByUserId_Success() {
        User userWithoutToken = new User(null, "user111", "lerida", new Tier(
            3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(userWithoutToken);
        AuthToken token = new AuthToken(1, "ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now());
        tokenService.createToken(token);
        Optional<AuthToken> foundToken = tokenService.findTokenByUserId(savedUser.getId());
        String foundTokenValue = "";
        if (foundToken.isPresent()) {
            foundTokenValue = foundToken.get().getValue();
        }
        assertTrue(foundToken.isPresent());
        assertTrue(encoder.matches(token.getValue(), foundTokenValue));
    }

    @Test
    void findByUserId_WrongId_EmptyResult() {
        assertEquals(Optional.empty(), tokenService.findTokenByUserId(888888888));
    }

    @Test
    void findByUserId_lessThanZeroId_Failure_Exception() {
        assertThrows(WrongCredentialsException.class, () -> tokenService.findTokenByUserId(-1));
    }

    @Test
    void saveTokenValue_Success() {
        User user = new User(null, "agamer", "lerida", new Tier(
            3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(user);
        AuthToken token = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now());
        tokenService.createToken(token);
        Optional<AuthToken> foundToken = tokenService.findTokenByUserId(savedUser.getId());
        String foundTokenValue = "";
        if (foundToken.isPresent()) {
            foundTokenValue = foundToken.get().getValue();
        }
        assertTrue(encoder.matches(token.getValue(), foundTokenValue));
    }
}

