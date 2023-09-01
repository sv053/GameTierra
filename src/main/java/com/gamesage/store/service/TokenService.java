package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.repository.TokenRepository;
import com.gamesage.store.exception.WrongCredentialsException;
import com.gamesage.store.util.TokenParser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenService {

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
			throw new WrongCredentialsException();
		}
		return tokenRepository.findByUserId(userId);
	}

	public AuthToken createToken(AuthToken authToken) {
		AuthToken tokenToSave = authToken.withTokenValue(authToken.getId(), encoder.encode(authToken.getValue()), authToken.getUserId(), authToken.getExpirationDateTime());
		AuthToken savedToken = tokenRepository.createOne(tokenToSave);
		int userId = savedToken.getUserId();
		String tokenValue = TokenParser.prepareHeader(authToken.getValue(), userId);
		return authToken.withTokenValue(savedToken.getId(), tokenValue, userId, authToken.getExpirationDateTime());
	}

	public AuthToken updateToken(AuthToken authToken) {
		return tokenRepository.updateByUserId(authToken);
	}

	public boolean matchTokens(AuthToken tokenFromUser, AuthToken existedToken) {
		return encoder.matches(tokenFromUser.getValue(), existedToken.getValue());
	}

	public boolean invalidateToken(AuthToken authToken) {
		Optional<AuthToken> tokenFromDatabase = findTokenByUserId(authToken.getUserId());
		if (tokenFromDatabase.isPresent()) {
			AuthToken existedToken = tokenFromDatabase.get();
			String tokenToInvalidate = TokenParser.findTokenValue(authToken.getValue());
			if (matchTokens(authToken, existedToken)) {
				return tokenRepository.removeByUserId(authToken.getUserId());
			}
		}
		return false;
	}
}

