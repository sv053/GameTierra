package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.WrongCredentialsException;
import com.gamesage.store.util.TokenParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TokenServiceIntegrationTest {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Test
    void findByUserId_Success() {
        User userWithoutToken = new User(null, "user111", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(userWithoutToken);
        AuthToken token = new AuthToken(1, "ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now());
        tokenService.createToken(token);
        String storedTokenValue = tokenService.findTokenByUserId(savedUser.getId())
                .map(AuthToken::getValue).orElse("");

        assertTrue(encoder.matches(token.getValue(), storedTokenValue));
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
        var savedToken = tokenService.createToken(token);

        assertNotNull(savedToken.getId());
        assertEquals(token.getValue(), TokenParser.findTokenValue(savedToken.getValue()));
    }
}

