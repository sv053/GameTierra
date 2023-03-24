package com.gamesage.store.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTmpl {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
