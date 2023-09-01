package com.gamesage.store.util;


import org.springframework.util.NumberUtils;

public final class TokenParser {

    private static final int USER_ID_PART_NUMBER = 0;
    private static final int TOKEN_VALUE_PART_NUMBER = 1;
    public static final String DELIMITER = "&";
    private static final String DELIMITER_REGEX = String.format("\\%s", DELIMITER);


    private TokenParser() {
    }

    private static String findTokenPart(String headerWithToken, int partNumber) {
        var tokenParts = headerWithToken.split(DELIMITER_REGEX);
        return tokenParts.length > partNumber ? tokenParts[partNumber] : "";
    }

    public static Integer findUserId(String headerWithToken) {
        int id = -1;
        if (headerWithToken == null || headerWithToken.isEmpty()) {
            id = NumberUtils.parseNumber(headerWithToken, Integer.class);
        }
        return id;//NumberUtils.isCreatable(findTokenPart(headerWithToken, USER_ID_PART_NUMBER), Integer.class, );
    }

    public static String findTokenValue(String headerWithToken) {
        return findTokenPart(headerWithToken, TOKEN_VALUE_PART_NUMBER);
    }

    public static String prepareHeader(String encodedToken, int userId) {
        return userId + DELIMITER + encodedToken;
    }
}

