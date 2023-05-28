package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class AuthService {

    private final UserService userService;
    private final TokenService tokenService;

    @Value("${com.gamesage.store.tokenExpiryInterval}")
    private int tokenExpiryInterval;

    @Autowired
    public AuthService(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public AuthToken authenticateUser(User user) {
        LocalDateTime localDateTime = LocalDateTime.now().plus(tokenExpiryInterval, ChronoUnit.DAYS);
        return provideWithToken(findUserId(user), localDateTime);
    }

    public void revokeAccess(String tokenToRemove) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        tokenService.invalidateToken(tokenToRemove);
    }

    private Integer findUserId(User user) {
        return userService.findByCredentials(user.getLogin(), user.getPassword()).getId();
    }

    private AuthToken provideWithToken(Integer id, LocalDateTime localDateTime) {
        return tokenService.findTokenById(id)
                .orElseGet(() -> {
                    try {
                        return tokenService.createToken(
                                new AuthToken(generateToken(), id, localDateTime));
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
    }

    private String generateToken() {
        return String.format("%s-%s", System.currentTimeMillis(), UUID.randomUUID());
    }
}

