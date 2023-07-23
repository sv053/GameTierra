package com.gamesage.store.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TokenParser {

    public static final int USER_ID_PART_NUMBER = 0;
    public static final int TOKEN_VALUE_PART_NUMBER = 1;
    public static final String DELIMITER = "&";
    private static final String DELIMITER_REGEX = String.format("\\%s", DELIMITER);

    private static BCryptPasswordEncoder encoder = null;

    public TokenParser(BCryptPasswordEncoder encoder) {
        TokenParser.encoder = encoder;
    }

    public static String findStringPart(String stringToFindPart, int partNumber) {
        return stringToFindPart.split(DELIMITER_REGEX)[partNumber];
    }

    public static String encodeToken(String rawToken) {
        return encoder.encode(rawToken);
    }

    public static String prepareTokenForHeader(String encodedToken, int userId) {
        return String.format("%s%s%s", userId, DELIMITER, encodedToken);
    }
}

