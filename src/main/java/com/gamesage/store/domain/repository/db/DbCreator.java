package com.gamesage.store.domain.repository.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbCreator{

    private static String[] dbScriptFileNames;
    private static List<String> queries;

    static {
        dbScriptFileNames = new String[]{
                "C:\\Users\\admin\\IdeaProjects\\GameTierra\\src\\main\\resources\\gamesage.sql",
                "C:\\Users\\admin\\IdeaProjects\\GameTierra\\src\\main\\resources\\gamesage1.sql"};
        queries = new ArrayList<>();
    }

    public static boolean splitContentIntoQueries() throws IOException {
        for (String fileName : dbScriptFileNames) {
            String fileContent = FileReader.readFile(fileName);
            queries.addAll(StringSplitter.removeSemicolon(fileContent));
        }
        if (queries.isEmpty()) return false;
        return true;
    }

    public static boolean recreateDb() throws IOException {
        if (!splitContentIntoQueries()) return false;

        try (final Connection connection = DbConnector.getConnection("jdbc:sqlite:gamesage.db")) {
            Class.forName("org.sqlite.JDBC");

            for (String query : queries) {
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
            }
        } catch (SQLException | ClassNotFoundException e) {
        }
        return true;
    }
}

