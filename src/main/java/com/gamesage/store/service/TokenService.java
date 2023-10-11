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
        logger.info(" userId - " + userId);
        if (0 >= userId) {
            throw new WrongCredentialsException();
        }
        return tokenRepository.findByUserId(userId);
    }

    public AuthToken createToken(AuthToken authToken) {
        AuthToken tokenToSave = authToken.withTokenValue(encoder.encode(authToken.getValue()));
        AuthToken savedToken = tokenRepository.createOne(tokenToSave);
        String tokenValue = TokenParser.prepareHeader(authToken.getValue(), savedToken.getUserId());

        AuthToken tokenHeader = authToken.withTokenValue(tokenValue).withId(savedToken.getId());
        logger.info(" token was created ");

        return tokenHeader;
    }

    public AuthToken updateTokenAndReturnHeader(AuthToken authToken) {
        AuthToken tokenWithEncodedValue = authToken.withTokenValue(encoder.encode(authToken.getValue()));
        tokenRepository.updateByUserId(tokenWithEncodedValue, authToken.getUserId());

        String tokenHeader = TokenParser.prepareHeader(authToken.getValue(), authToken.getUserId());
        return authToken.withTokenValue(tokenHeader);
    }

    public boolean matchTokens(AuthToken tokenFromUser, AuthToken existedToken) {
        boolean ifMatches = encoder.matches(tokenFromUser.getValue(), existedToken.getValue());
        if (ifMatches) {
            logger.warn(" tokens do not match ");
        }
        return ifMatches;
    }

    public boolean invalidateToken(AuthToken authToken) {
        logger.info(" check if token is valid ");

        Optional<AuthToken> tokenFromDatabase = findTokenByUserId(authToken.getUserId());
        if (tokenFromDatabase.isPresent()) {
            logger.info(" token exists in db ");

            AuthToken existedToken = tokenFromDatabase.get();
            TokenParser.findTokenValue(authToken.getValue());
            if (matchTokens(authToken, existedToken)) {
                logger.info(" token was invalidated ");

                return tokenRepository.removeByUserId(authToken.getUserId());
            }
        }
        return false;
    }
}

