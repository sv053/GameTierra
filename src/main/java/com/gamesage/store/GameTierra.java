package com.gamesage.store;

import com.gamesage.store.domain.repository.db.DbCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class GameTierra {

    public static final Logger logger = LoggerFactory.getLogger(GameTierra.class);
    public static void main(String[] args) {

        SpringApplication.run(GameTierra.class);
        try {
            DbCreator.recreateDb();
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }
}

