package com.gamesage.store.service.dbsettings;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Service;

@Service
public class DataSource {

    @Bean
    private JdbcTemplate jdbcTemplate;

    public DataSource() {
        this.jdbcTemplate = connect();
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    private JdbcTemplate connect(){
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.sqlite.JDBC.class);
        dataSource.setUrl("jdbc:sqlite:gamesage.db");

        return new JdbcTemplate(dataSource);

    }
}

