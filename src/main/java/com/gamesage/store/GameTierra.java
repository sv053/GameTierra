package com.gamesage.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootApplication
public class GameTierra {

    private static final Logger logger = LoggerFactory.getLogger(GameTierra.class);

    public static void main(String[] args) {
        SpringApplication.run(GameTierra.class);

        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:aceofgames.db");
            if (connection != null) {
                logger.info("Database is connected");
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}

