package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.repository.TokenRepository;
import com.gamesage.store.exception.WrongCredentialsException;
import com.gamesage.store.util.TokenParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final TokenRepository tokenRepository;
    private final BCryptPasswordEncoder encoder;

    public TokenService(TokenRepository tokenRepository, BCryptPasswordEncoder encoder) {
        this.tokenRepository = tokenRepository;
        this.encoder = encoder;
    }

    public Optional<AuthToken> findTokenById(Integer id) {
        return tokenRepository.findById(id);
    }

    public Optional<AuthToken> findTokenByUserId(Integer userId) {
        if (0 >= userId) {
            logger.error("Wrong token: no user with id {} exists ", userId);
            throw new WrongCredentialsException();
        }
        return tokenRepository.findByUserId(userId);
    }

    public AuthToken createToken(AuthToken authToken) {
        AuthToken tokenToSave = authToken.withTokenValue(encoder.encode(authToken.getValue()));
        AuthToken savedToken = tokenRepository.createOne(tokenToSave);
        String tokenValue = TokenParser.prepareHeader(authToken.getValue(), savedToken.getUserId());
        AuthToken tokenHeader = savedToken.withTokenValue(tokenValue);
        logger.info("Token for user with id {} was created ", tokenHeader.getUserId());

        return tokenHeader;
    }

    public AuthToken updateTokenAndReturnHeader(AuthToken authToken) {
        AuthToken tokenWithEncodedValue = authToken.withTokenValue(encoder.encode(authToken.getValue()));
        tokenRepository.updateByUserId(tokenWithEncodedValue, authToken.getUserId());

        String tokenHeader = TokenParser.prepareHeader(authToken.getValue(), authToken.getUserId());
        return authToken.withTokenValue(tokenHeader);
    }

    public boolean invalidateToken(AuthToken authToken) {
        Optional<AuthToken> tokenFromDatabase = findTokenByUserId(authToken.getUserId());
        if (tokenFromDatabase.isPresent()) {
            AuthToken existedToken = tokenFromDatabase.get();
            boolean tokensMatch = encoder.matches(authToken.getValue(), existedToken.getValue());
            if (tokensMatch) {
                logger.info("Token for user with id {} is preparing to be invalidated", existedToken.getUserId());
                return tokenRepository.removeByUserId(authToken.getUserId());
            }
        }
        return false;
    }
}

