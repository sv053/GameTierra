package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.repository.TokenRepository;
import com.gamesage.store.exception.WrongCredentialsException;
import com.gamesage.store.util.TokenParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        logger.info("findTokenByUserId# userId - " + userId);
        if (0 >= userId) {
            throw new WrongCredentialsException();
        }
        return tokenRepository.findByUserId(userId);
    }

    public AuthToken createToken(AuthToken authToken) {
        AuthToken tokenToSave = authToken.withTokenValue(authToken, encoder.encode(authToken.getValue()));
        AuthToken savedToken = tokenRepository.createOne(tokenToSave);
        int userId = savedToken.getUserId();
        String tokenValue = TokenParser.prepareHeader(authToken.getValue(), userId);

        AuthToken tokenHeader = authToken.withTokenValue(authToken, tokenValue);
        logger.info("#createToken createdToken - " + tokenHeader);

        return tokenHeader;
    }

    public AuthToken updateTokenAndReturnHeader(AuthToken authToken) {
        AuthToken tokenWithEncodedValue = authToken.withTokenValue(authToken, encoder.encode(authToken.getValue()));
        tokenRepository.updateByUserId(tokenWithEncodedValue, authToken.getUserId());

        String tokenHeader = TokenParser.prepareHeader(authToken.getValue(), authToken.getUserId());
        return authToken.withTokenValue(authToken, tokenHeader);
    }

    public boolean matchTokens(AuthToken tokenFromUser, AuthToken existedToken) {
        boolean ifMatches = encoder.matches(tokenFromUser.getValue(), existedToken.getValue());
        if (ifMatches) {
            logger.warn(" #matchTokens tokens do not match: " + tokenFromUser + " | " + existedToken);
        }
        return ifMatches;
    }

    public boolean invalidateToken(AuthToken authToken) {
        logger.info(LocalDateTime.now() + " TokenService#invalidateToken : check token " + authToken);

        Optional<AuthToken> tokenFromDatabase = findTokenByUserId(authToken.getUserId());
        if (tokenFromDatabase.isPresent()) {
            logger.info("#invalidateToken : token exists in db " + authToken);

            AuthToken existedToken = tokenFromDatabase.get();
            String tokenToInvalidate = TokenParser.findTokenValue(authToken.getValue());
            if (matchTokens(authToken, existedToken)) {
                logger.info("#invalidateToken token was invalidated : " + tokenToInvalidate);

                return tokenRepository.removeByUserId(authToken.getUserId());
            }
        }
        return false;
    }
}

