package com.gamesage.store.controller;

import com.gamesage.store.domain.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LogoutControllerIntegrationTest extends ControllerIntegrationTest {

    private static final String TOKEN_HEADER_TITLE = "X-Auth-Token";
    protected static final String LOGOUT_ENDPOINT = "/logout";

    private String userJson;
    private User user;
    private String token;

    @BeforeAll
    void setup() throws Exception {
        userJson = Files.readString(Path.of(userJsonResource.getURI()));
        User userToSave = objectMapper.readValue(userJson, User.class);
        user = userService.createOne(userToSave);
        token = loginAndGetToken(userJson);
    }

    @Test
    void givenLoggedInUser_shouldLogout() throws Exception {
        mockMvc.perform(post(LOGOUT_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TOKEN_HEADER_TITLE, token)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk());
    }
}