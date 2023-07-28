package com.gamesage.store.util;

public class TokenParser {

    public static final int USER_ID_PART_NUMBER = 0;
    public static final int TOKEN_VALUE_PART_NUMBER = 1;
    public static final String DELIMITER = "&";
    private static final String DELIMITER_REGEX = String.format("\\%s", DELIMITER);

    public static String findStringPart(String stringToFindPart, int partNumber) {
        return stringToFindPart.split(DELIMITER_REGEX)[partNumber];
    }

    public static Integer convertStringToInteger(String stringToConvert, int partNumber) {
        return Integer.parseInt(findStringPart(stringToConvert, partNumber));
    }

    public static String prepareTokenForHeader(String encodedToken, int userId) {
        return String.format("%s%s%s", userId, DELIMITER, encodedToken);
    }
}

