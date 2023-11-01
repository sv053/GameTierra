package com.gamesage.store.util;


import com.gamesage.store.exception.WrongTokenException;
import org.apache.logging.log4j.util.Strings;

public final class TokenParser {

    private static final String DELIMITER = "&";
    private static final int USER_ID_PART_NUMBER = 0;
    private static final int TOKEN_VALUE_PART_NUMBER = 1;


    private TokenParser() {
    }

    public static String prepareHeader(String token, int userId) {
        return userId + DELIMITER + token;
    }

    public static Integer findUserId(String headerWithToken) {
        String id = findTokenPart(headerWithToken, USER_ID_PART_NUMBER);
        if (id.matches("^[1-9]\\d*$")) {
            return Integer.parseInt(id);
        }
        throw new WrongTokenException();
    }

    public static String findTokenValue(String headerWithToken) {
        var token = findTokenPart(headerWithToken, TOKEN_VALUE_PART_NUMBER);
        if (Strings.isBlank(token)) {
            throw new WrongTokenException();
        }
        return token;
    }

    private static String findTokenPart(String headerWithToken, int partNumber) {
        if (Strings.isBlank(headerWithToken) || !headerWithToken.contains(DELIMITER)) {
            throw new WrongTokenException();
        }
        var tokenParts = headerWithToken.split(DELIMITER);
        return tokenParts.length > partNumber ? tokenParts[partNumber] : "";
    }
}

