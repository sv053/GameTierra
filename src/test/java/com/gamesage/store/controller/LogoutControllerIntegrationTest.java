package com.gamesage.store.controller;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.EntityNotFoundException;
import com.gamesage.store.service.TokenService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LogoutControllerIntegrationTest extends ControllerIntegrationTest {

    private String userJson;
    private User user;

    @Autowired
    private TokenService tokenService;

    @BeforeAll
    void setup() throws IOException {
        userJson = Files.readString(Path.of(userJsonResource.getURI()));
        user = objectMapper.readValue(userJson, User.class);
    }

    @Test
    void givenLoggedInUser_shouldLogout() throws Exception {
        User savedUser = userService.createOne(user);
        String actualToken = loginAndGetToken(userJson);

        mockMvc.perform(post("/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized());

        assertThrows(EntityNotFoundException.class, () -> tokenService
                .findTokenById(savedUser.getId())
                .map(AuthToken::getValue)
                .orElseThrow(() -> new EntityNotFoundException("token for " + user.getLogin())));
    }
}