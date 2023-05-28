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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
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
    void findByTokenValue_Success() throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        User userWithoutToken = new User(null, "user1", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(userWithoutToken);
        AuthToken tokenToCreate = new AuthToken("ftyytgiuhiuhiuh", savedUser.getId(), LocalDateTime.now());
        AuthToken savedToken = tokenService.createToken(tokenToCreate);
        String foundToken = tokenService.findToken(savedToken.getValue()).getValue();

        assertTrue(encoder.matches(tokenToCreate.getValue(), foundToken));
    }

    @Test
    void findByLogin_Success() throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        User userWithoutToken = new User(null, "user111", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(userWithoutToken);
        AuthToken token = new AuthToken(encoder.encode("ftyytgiuhiuhiuh"), savedUser.getId(), LocalDateTime.now());
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
    void saveTokenValue_Success() throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        User user = new User(null, "agamer", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(user);
        AuthToken token = new AuthToken("ftyytgiuhiuhiuh", savedUser.getId(), LocalDateTime.now());
        tokenService.createToken(token);
        AuthToken foundToken = tokenService.findTokenById(savedUser.getId())
                .orElseThrow(WrongCredentialsException::new);

        assertTrue(encoder.matches(token.getValue(), foundToken.getValue()));
    }
}

