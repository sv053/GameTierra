package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.WrongCredentialsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TokenServiceIntegrationTest {

    protected static final String DELIMITER = "&";

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
        AuthToken foundToken = tokenService.findToken(tokenToFind.getValue());

        assertTrue(encoder.matches(tokenToCreate.getValue(), foundToken.getValue()));
    }

    @Test
    void findByLogin_Success() {
        User userWithoutToken = new User(null, "user111", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(userWithoutToken);
        AuthToken token = new AuthToken(1, "ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now());
        tokenService.createToken(token);
        Optional<AuthToken> foundToken = tokenService.findTokenById(savedUser.getId());
        String foundTokenValue = "";
        if (foundToken.isPresent()) {
            foundTokenValue = foundToken.get().getValue();
        }

        assertTrue(foundToken.isPresent());
        assertTrue(encoder.matches(token.getValue(), foundTokenValue));
    }

    @Test
    void findByLogin_Failure_Exception() {
        assertThrows(WrongCredentialsException.class, () -> tokenService.findToken("123" + DELIMITER + "cfgjgvuikhyvbfbu"));
    }

    @Test
    void saveTokenValue_Success() {
        User user = new User(null, "agamer", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(user);
        AuthToken token = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now());
        tokenService.createToken(token);
        AuthToken foundToken = (tokenService.findTokenById(savedUser.getId()).get());

        assertTrue(encoder.matches(token.getValue(), foundToken.getValue()));
    }

    @Test
    void removeExpiredTokens_Success() {
        User user = new User(null, "agamer", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(user);
        AuthToken token = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now().minus(100, ChronoUnit.DAYS));
        AuthToken savedToken = tokenService.createToken(token);
        AuthToken foundToken = (tokenService.findTokenById(savedUser.getId())).get();

        assertTrue(encoder.matches(token.getValue(), foundToken.getValue()));

        tokenService.removeExpiredTokens();

        assertEquals(Optional.empty(), tokenService.findTokenById(0));
    }

    @Test
    void invalidateToken_Success() {
        User user = new User(null, "agamer", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(user);
        AuthToken token = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now());
        AuthToken savedToken = tokenService.createToken(token);
        AuthToken foundToken = (tokenService.findTokenById(savedUser.getId())).get();

        assertTrue(encoder.matches(token.getValue(), foundToken.getValue()));

        var result = tokenService.invalidateToken(savedToken);
        var t = tokenService.findToken(savedToken.getValue());
        assertThrows(WrongCredentialsException.class, () -> tokenService.findToken(savedToken.getValue()));
    }
}

