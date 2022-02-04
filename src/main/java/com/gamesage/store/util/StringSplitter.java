package com.gamesage.store.util;

import java.util.Arrays;
import java.util.List;

public class StringSplitter {

    public static List<String> splitFileContent(String fileContent) {

        return Arrays.asList(fileContent.trim().split(";"));
    }

    public static List<String> removeSemicolon(String fileContent) {

        List<String> stringWithoutSemicolon = splitFileContent(fileContent);
        stringWithoutSemicolon.forEach(s -> s.replace(';', ' '));
        return stringWithoutSemicolon;
    }
}

