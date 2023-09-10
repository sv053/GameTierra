package com.gamesage.store.util;

import com.gamesage.store.exception.NoTokenHeaderException;
import com.gamesage.store.exception.WrongCredentialsException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenParserTest {

	@Test
	void findUserId_EmptyInput_Exception() {
		String empty = "";

		assertThrows(NoTokenHeaderException.class, () -> TokenParser.findUserId(empty));
	}

	@Test
	void testFindUserId() {
		int expectedUserId = 111;
		String headerWithToken = expectedUserId + "&eszrdgkuop";

		Integer userId = TokenParser.findUserId(headerWithToken);

		assertEquals(expectedUserId, userId);
	}

	@Test
	void testNotFoundUserId() {
		String expectedUserId = "wrongid";
		String headerWithToken = expectedUserId + "&eszrdgkuop";

		assertThrows(WrongCredentialsException.class, () -> TokenParser.findUserId(headerWithToken));
	}

	@Test
	void testFindUserIdWithInvalidHeader() {
		String wrongFormatHeaderWithToken = "iuhiuiijo";

		assertThrows(NoTokenHeaderException.class, () -> TokenParser.findUserId(wrongFormatHeaderWithToken));
	}
}