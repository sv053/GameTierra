package com.gamesage.store.util;

public final class TokenParser {

    public static final int USER_ID_PART_NUMBER = 0;
    public static final int TOKEN_VALUE_PART_NUMBER = 1;
    public static final String DELIMITER = "&";
    private static final String DELIMITER_REGEX = String.format("\\%s", DELIMITER);

    private TokenParser() {
    }

    private static String findTokenPart(String headerWithToken, int partNumber) {
        return headerWithToken.split(DELIMITER_REGEX)[partNumber];
    }

    private static String findUserId(String headerWithToken) {
        return findTokenPart(headerWithToken, USER_ID_PART_NUMBER);
    }

    public static String findTokenValue(String headerWithToken) {
        return findTokenPart(headerWithToken, TOKEN_VALUE_PART_NUMBER);
    }

    public static Integer convertUserIdToInteger(String userId) {
        return Integer.parseInt(findUserId(userId));
    }

    public static String prepareHeader(String encodedToken, int userId) {
        return userId + DELIMITER + encodedToken;
    }
}

