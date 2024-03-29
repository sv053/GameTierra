package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gamesage.store.domain.model.Card;
import com.gamesage.store.domain.model.ResponseError;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.paymentapi.PaymentRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIntegrationTest extends ControllerIntegrationTest {

    private static final String API_USER_ENDPOINT = "/users";
    private static final String TOPUP_ENDPOINT = "/users/{userId}/topup";

    @Value("classpath:request/user/nonExistentUser.json")
    public Resource notUserJsonResource;
    private User user;
    private String token;

    @BeforeAll
    void setup() throws Exception {
        objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        String userJson = Files.readString(Path.of(userJsonResource.getURI()));
        User userToCreate = objectMapper.readValue(userJson, User.class);
        user = userService.createOne(userToCreate);
        token = loginAndGetToken(userJson);
    }

    @Test
    void givenAuthorizedUser_whenFindAllUsers_thenSuccess() throws Exception {
        User secondUser = new User(null, "second", "secondPass", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User secondSavedUser = userService.createOne(secondUser);

        mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TOKEN_HEADER_NAME, token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].login").value(user.getLogin()))
                .andExpect(jsonPath("$[1].login").value(secondUser.getLogin()));
    }

    @Test
    void givenUserWithoutToken_whenFindAllUsers_thenForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenUnauthorizedUser_whenFindAllUsers_thenForbidden() throws Exception {
        String wrongToken = "1" + DELIMITER + "cfgjgvuikhyvbfbu";

        mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TOKEN_HEADER_NAME, wrongToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void createOne_thenSuccess() throws Exception {
        String nonExistentUserJson = Files.readString(Path.of(notUserJsonResource.getURI()));
        User userUnsaved = objectMapper.readValue(nonExistentUserJson, User.class);

        mockMvc.perform(post(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(nonExistentUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(userUnsaved.getLogin()))
                .andExpect(jsonPath("$.tier.level").value(userUnsaved.getTier().getLevel()))
                .andExpect(jsonPath("$.tier.id").value(userUnsaved.getTier().getId()))
                .andExpect(jsonPath("$.tier.cashbackPercentage").value(userUnsaved.getTier().getCashbackPercentage()));
    }

    @Test
    void givenUser_whenTryTopUp_thenFails() throws Exception {
        Card cardWithWrongNumber = new Card(156L,
                "Jack Black",
                LocalDate.of(2025, 3, 30),
                111);
        PaymentRequest errorPaymentRequest = new PaymentRequest(BigDecimal.ONE, cardWithWrongNumber);

        mockMvc.perform(post(TOPUP_ENDPOINT, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(errorPaymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").isNotEmpty())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.responseError").value(ResponseError.INVALID_CARD_NUMBER.getCardErrorMessage()));
    }

    @Test
    void givenUser_whenTryTopUp_thenSuccess() throws Exception {
        Card cardWithCorrectNumber = new Card(1567123425635896L,
                "Jack Black",
                LocalDate.of(2025, 3, 30),
                111);
        PaymentRequest okPaymentRequest = new PaymentRequest(BigDecimal.ONE, cardWithCorrectNumber);
        mockMvc.perform(post(TOPUP_ENDPOINT, user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(okPaymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transactionId").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseError").isEmpty());
    }
}

