package com.gamesage.store.domain.repository.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnector {

    public static Connection getConnection(String dbUrl) throws SQLException {
        return DriverManager.getConnection(dbUrl);
    }
}

