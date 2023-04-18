package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginControllerIntegrationTest {

    private static final String API_ENDPOINT = "/login";
    private static final String TOKEN_HEADER_NAME = "X-Auth-Token";
    @Value("classpath:request/user/test.json")
    private Resource userJsonResource;

    private String userJson;
    private User user;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeAll
    void setup() throws IOException {
        userJson = Files.readString(Path.of(userJsonResource.getURI()));
        user = objectMapper.readValue(userJson, User.class);
    }

    @Test
    void givenCorrectCreds_shouldLoginAndReturn200() throws Exception {
        userService.deleteAll();

        userService.createOne(user);
        String tokenResponseValue = mockMvc.perform(post(API_ENDPOINT)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(TOKEN_HEADER_NAME);

        Optional<String> tokenValue = tokenService.findTokenByLogin(user.getLogin())
                .map(AuthToken::getValue);

        assertNotNull(tokenResponseValue);
        assertFalse(tokenResponseValue.isBlank());
        assertEquals(tokenValue, Optional.of(tokenResponseValue));
    }

    @Test
    void givenUserCredsDoNotExist_shouldNotLoginAndReturn401() throws Exception {
        userService.deleteAll();
        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

//    @Test
//    void testUserTable() {
//        List<Map<String, Object>> rows = jdbcTemplate.queryForList("SELECT * FROM user");
//
//        for (Map<String, Object> row : rows) {
//            System.out.println(row);
//            assertTrue(row.containsValue(user.getLogin()));
//
//        }
//        assertEquals(1, rows.size());
//    }
}

