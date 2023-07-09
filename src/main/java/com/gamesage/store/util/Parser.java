package com.gamesage.store.util;

public class Parser {

    public static String DELIMITER = "&";

    public static String findSubstring(String stringToParse, int partNumber) {
        String delimiterRegex = String.format("\\%s", DELIMITER);
        String result = "";
        if (stringToParse.contains(DELIMITER)) {
            result = stringToParse.split(delimiterRegex)[partNumber];
        }
        return result;
    }
}
