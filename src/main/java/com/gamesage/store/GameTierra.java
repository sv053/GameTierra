package com.gamesage.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GameTierra {

    public static void main(String[] args) {
        SpringApplication.run(GameTierra.class);
    }
}

