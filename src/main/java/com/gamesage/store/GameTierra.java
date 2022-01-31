package com.gamesage.store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@SpringBootApplication
public class GameTierra {

    public static final Logger logger = LoggerFactory.getLogger(GameTierra.class);

    public static void main(String[] args) {

        SpringApplication.run(GameTierra.class);

    }
//    @Value(value = "#{gamesage}")
//    public static DataSource dataSource;
//
    @Bean
    public static DataSource getDataSource() {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url("jdbc:sqlite:gamesage.db");
        dataSourceBuilder.driverClassName("org.sqlite.JDBC");
        return dataSourceBuilder.build();
    }

    @Bean
    public static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }
}

