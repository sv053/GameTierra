package com.gamesage.store.security.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.WrongCredentialsException;
import com.gamesage.store.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceIntegrationTest {

	@Autowired
	private AuthService authService;
	@Autowired
	private UserService userService;

	@Test
	void authenticateUser_Success() {
		User user = new User(0, "user11111", "lerida", new Tier(
				3, "SILVER", 10.d), BigDecimal.TEN);
		User savedUser = userService.createOne(user);
		assertNotNull(userService.findByLogin(user.getLogin()));

		AuthToken foundToken = (authService.authenticateUser(new User(
				0, "user11111", "lerida", new Tier(
				3, "SILVER", 10.d), BigDecimal.TEN
		)));
		assertNotNull(foundToken);
		assertNotNull(foundToken.getValue());
		assertEquals(savedUser.getId(), foundToken.getUserId());
	}

	@Test
	void authenticateUser_Exception() {
		User user = new User(null, "user1", "lerida", new Tier(
				3, "SILVER", 10.d), BigDecimal.TEN);

		assertThrows(WrongCredentialsException.class, () -> authService.authenticateUser(user));
	}
}

