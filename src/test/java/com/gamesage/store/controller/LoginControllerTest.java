package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class LoginControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final String API_ENDPOINT = "/login";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Test
    void givenCorrectCreds_shouldLoginAndReturn200() throws Exception {
        User userWithRightCreds = new User(111, "admin", "letmein", new Tier(
                1, "FREE", 0.d), BigDecimal.TEN);

        userService.createOne(userWithRightCreds);

        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(userWithRightCreds))
                        .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void givenUserDoesNotExist_shouldNotLoginAndReturn401() throws Exception {
        User userWithRightCreds = new User(111, "admin", "letmein", new Tier(
                1, "FREE", 0.d), BigDecimal.TEN);

        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(userWithRightCreds))
                        .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    void givenCorrectLoginAndWrongKey_shouldReturn401() throws Exception {
        User userWithRightCreds = new User(1, "admin", "letmein", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        userService.createOne(userWithRightCreds);

        User userWithWrongCredsJson = new User(null, "admin", "neverletmein", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);

        ObjectMapper mapper = new ObjectMapper();
        String userJson = mapper.writeValueAsString(userWithWrongCredsJson);

        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(userJson)
                        .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    void givenWrongLoginAndCorrectKey_shouldReturn401() throws Exception {
        User userWithRightCreds = new User(1, "admin", "letmein", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        userService.createOne(userWithRightCreds);

        User userWithWrongCreds = new User(2, "hijacker", "letmein", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);

        String userJson = new ObjectMapper().writeValueAsString(userWithWrongCreds);
        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(userJson)
                        .accept(APPLICATION_JSON_UTF8))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}

