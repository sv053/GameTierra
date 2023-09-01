package com.gamesage.store.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenParserTest {

	@Test
	void findUserId_EmptyInput_NumberFormatException() {
		String empty = "";

		assertThrows(NumberFormatException.class, () -> TokenParser.findUserId(empty));
	}

	@Test
	void testFindUserId() {
		int expectedUserId = 111;
		String headerWithToken = expectedUserId + "&eszrdgkuop";

		Integer userId = TokenParser.findUserId(headerWithToken);

		assertEquals(expectedUserId, userId);
	}

	@Test
	void testFindUserIdWithInvalidHeader() {
		String headerWithToken = "iuhiuiijo";

		Integer userId = TokenParser.findUserId(headerWithToken);

		assertNull(userId);
	}
}