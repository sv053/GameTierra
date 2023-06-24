package com.gamesage.store.util;

public class Parser {

    public static String findSubstring(String stringToParse, String delimiter, String regex, int partNumber) {
        String result = "";
        if (stringToParse.contains(delimiter)) {
            result = stringToParse.split(regex)[partNumber];
        }
        return result;
    }
}
