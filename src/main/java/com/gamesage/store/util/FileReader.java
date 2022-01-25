package com.gamesage.store.util;

import java.io.BufferedReader;
import java.io.IOException;

public class FileReader {

    public static String readFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new java.io.FileReader(filePath));
        StringBuilder fileContent = new StringBuilder();

        while (reader.ready()){
            fileContent.append(reader.readLine());
        }
        return fileContent.toString();
    }
}

