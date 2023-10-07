package com.gamesage.store.util;

import com.gamesage.store.exception.NoTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenParserTest {

    @Test
    void findUserId_EmptyInput_Exception() {
        String empty = "";

        assertThrows(NoTokenException.class, () -> TokenParser.findUserId(empty));
    }

    @Test
    void findUserId_EmptyButDelimiter_Exception() {
        String empty = "&";

        assertThrows(NoTokenException.class, () -> TokenParser.findUserId(empty));
    }

    @Test
    void findUserId_ZeroInput_Exception() {
        String empty = "0&eszrdgkuop";

        assertThrows(NoTokenException.class, () -> TokenParser.findUserId(empty));
    }

    @Test
    void findUserId_NegativeInput_Exception() {
        String empty = "-111&eszrdgkuop";

        assertThrows(NoTokenException.class, () -> TokenParser.findUserId(empty));
    }

    @Test
    void findUserId_NaNInput_Exception() {
        String empty = "cjkx_&eszrdgkuop";

        assertThrows(NoTokenException.class, () -> TokenParser.findUserId(empty));
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

        assertThrows(NoTokenException.class, () -> TokenParser.findUserId(headerWithToken));
    }

    @Test
    void testFindUserIdWithInvalidHeader() {
        String wrongFormatHeaderWithToken = "iuhiuiijo";

        assertThrows(NoTokenException.class, () -> TokenParser.findUserId(wrongFormatHeaderWithToken));
    }
}