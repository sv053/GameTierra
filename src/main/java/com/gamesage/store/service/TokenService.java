package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.repository.TokenRepository;
import com.gamesage.store.exception.WrongCredentialsException;
import com.gamesage.store.security.service.TokenEncoderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private final TokenEncoderService tokenEncoderService;

    public TokenService(TokenRepository tokenRepository, TokenEncoderService tokenEncoderService) {
        this.tokenRepository = tokenRepository;
        this.tokenEncoderService = tokenEncoderService;
    }

    public Optional<AuthToken> findTokenById(Integer userId) {
        return tokenRepository.findById(userId);
    }

    public AuthToken findToken(String token) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        return tokenRepository.findByValue(encodeToken(token))
                .orElseThrow(WrongCredentialsException::new);
    }

    private String encodeToken(String token) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        return Arrays.toString(tokenEncoderService.encryptToken(token.getBytes(StandardCharsets.UTF_8)));
    }

    public AuthToken createToken(AuthToken authToken) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        AuthToken tokenToSave = new AuthToken(
                encodeToken(authToken.getValue()),
                authToken.getUserId(),
                authToken.getExpirationDate());
        return tokenRepository.createOne(tokenToSave);
    }

    @Scheduled(cron = "${com.gamesage.store.cleanup}")
    public void removeExpiredTokens() {
        tokenRepository.removeExpired();
    }

    public void invalidateToken(String token) throws InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        tokenRepository.removeByValue(findToken(token).getValue());
    }

}

