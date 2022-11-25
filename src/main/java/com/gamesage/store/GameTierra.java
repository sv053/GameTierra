package com.gamesage.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class GameTierra {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(GameTierra.class);
//
//        String[] beanNames = ctx.getBeanDefinitionNames();
//        for (String beanName : beanNames) {
//            System.out.println(beanName);
//        }
    }
}

