package com.gamesage.store.util;


import com.gamesage.store.exception.WrongTokenException;
import org.apache.logging.log4j.util.Strings;

import java.util.Arrays;

public final class TokenParser {

    private static final String DELIMITER = "&";
    private static final int USER_ID_PART_NUMBER = 0;
    private static final int VALUE_PART_NUMBER = 1;


    private TokenParser() {
    }

    public static String prepareHeader(String token, int userId) {
        return userId + DELIMITER + token;
    }

    public static Integer findUserId(String headerWithToken) {
        return Integer.parseInt(findTokenPart(headerWithToken, USER_ID_PART_NUMBER));
    }

    public static String findTokenValue(String headerWithToken) {
        return findTokenPart(headerWithToken, VALUE_PART_NUMBER);
    }

    private static String findTokenPart(String headerWithToken, int partNumber) {
        if (Strings.isBlank(headerWithToken) || !headerWithToken.contains(DELIMITER)) {
            throw new WrongTokenException();
        }
        var tokenParts = headerWithToken.split(DELIMITER);

        String result = Arrays.stream(tokenParts)
            .skip(partNumber)
            .findFirst()
            .orElse("");

        if ((partNumber == USER_ID_PART_NUMBER && !result.matches("^[1-9]\\d*$"))
            || (partNumber == VALUE_PART_NUMBER && Strings.isBlank(result))) {
            throw new WrongTokenException();
        }

        return result;
    }
}

