package com.gamesage.store.dao;

import com.gamesage.store.GameTierra;
import com.gamesage.store.util.FileReader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DbInitializer {

    public final JdbcTemplate jdbcTemplate;

    public DbInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void init() {
        String outerPath = System.getProperty("user.dir").toString();
        String innerPath = "\\src\\main\\resources\\gamesage1.sql";
        String query = "";
        try {
            query = FileReader.readFile(outerPath + innerPath);
        } catch (
                IOException e) {
            GameTierra.logger.error(e.getMessage());
        }
        jdbcTemplate.execute(query);
    }
}

