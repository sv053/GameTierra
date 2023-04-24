package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesage.store.service.GameService;
import com.gamesage.store.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class ControllerIntegrationTest {

    protected static final String TOKEN_HEADER_NAME = "X-Auth-Token";
    protected static final String LOGIN_ENDPOINT = "/login";

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected UserService userService;
    @Autowired
    protected GameService gameService;

    @BeforeAll
    abstract void setup() throws Exception;

    String loginAndGetToken(String jsonObject) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_ENDPOINT)
                        .content(jsonObject)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(TOKEN_HEADER_NAME);
    }

    @AfterAll
    abstract void tearDown();
}

