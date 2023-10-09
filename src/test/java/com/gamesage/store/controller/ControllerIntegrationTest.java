package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesage.store.service.GameService;
import com.gamesage.store.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource("classpath:application-test.properties")
abstract public class ControllerIntegrationTest {

    protected static final String TOKEN_HEADER_NAME = "X-Auth-Token";
    protected static final String LOGIN_ENDPOINT = "/login";
    protected static final String DELIMITER = "&";


    @Value("classpath:request/user/existentUser.json")
    protected Resource userJsonResource;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected UserService userService;
    @Autowired
    protected GameService gameService;

    public String loginAndGetToken(String jsonObject) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_ENDPOINT)
                .content(jsonObject)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getHeader(TOKEN_HEADER_NAME);
    }

    @AfterAll
    void tearDown() {
        userService.deleteAll();
        gameService.deleteAll();
    }
}

