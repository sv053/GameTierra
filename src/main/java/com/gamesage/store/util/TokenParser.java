package com.gamesage.store.util;


import com.gamesage.store.exception.WrongTokenException;
import org.springframework.util.StringUtils;

public final class TokenParser {

    public static final String DELIMITER = "&";
    private static final int USER_ID_PART_NUMBER = 0;
    private static final int TOKEN_VALUE_PART_NUMBER = 1;
    private static final String DELIMITER_REGEX = String.format("\\%s", DELIMITER);


    private TokenParser() {
    }

    private static String findTokenPart(String headerWithToken, int partNumber) {
        if (!StringUtils.hasText(headerWithToken) || !headerWithToken.contains(DELIMITER)) {
            throw new WrongTokenException(headerWithToken);
        }
        var tokenParts = headerWithToken.split(DELIMITER_REGEX);
        return tokenParts.length > partNumber ? tokenParts[partNumber] : "";
    }

    public static Integer findUserId(String headerWithToken) {
        String id = findTokenPart(headerWithToken, USER_ID_PART_NUMBER);
        if (id.matches("^[1-9]\\d*$")) {
            return Integer.parseInt(id);
        } else {
            throw new WrongTokenException(headerWithToken);
        }
    }

    public static String findTokenValue(String headerWithToken) {
        var token = findTokenPart(headerWithToken, TOKEN_VALUE_PART_NUMBER);
        if (!StringUtils.hasLength(token)) {
            throw new WrongTokenException(token);
        }
        return token;
    }

    public static String prepareHeader(String token, int userId) {
        return userId + DELIMITER + token;
    }
}

