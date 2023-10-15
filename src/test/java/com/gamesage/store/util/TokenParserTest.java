package com.gamesage.store.util;

import com.gamesage.store.exception.WrongTokenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TokenParserTest {

    @Test
    void findUserId_EmptyInput_Exception() {
        String empty = "";

        assertThrows(WrongTokenException.class, () -> TokenParser.findUserId(empty));
    }

    @Test
    void findUserId_DelimiterOnly_Exception() {
        String delimiterOnly = "&";

        assertThrows(WrongTokenException.class, () -> TokenParser.findUserId(delimiterOnly));
    }

    @Test
    void findUserId_ZeroInput_Exception() {
        String empty = "0&eszrdgkuop";

        assertThrows(WrongTokenException.class, () -> TokenParser.findUserId(empty));
    }

    @Test
    void findUserId_NegativeInput_Exception() {
        String empty = "-111&eszrdgkuop";

        assertThrows(WrongTokenException.class, () -> TokenParser.findUserId(empty));
    }

    @Test
    void findUserId_NaNInput_Exception() {
        String empty = "cjkx_&eszrdgkuop";

        assertThrows(WrongTokenException.class, () -> TokenParser.findUserId(empty));
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

        assertThrows(WrongTokenException.class, () -> TokenParser.findUserId(headerWithToken));
    }

    @Test
    void testFindUserIdWithInvalidHeader() {
        String wrongFormatHeaderWithToken = "iuhiuiijo";

        assertThrows(WrongTokenException.class, () -> TokenParser.findUserId(wrongFormatHeaderWithToken));
    }
}

