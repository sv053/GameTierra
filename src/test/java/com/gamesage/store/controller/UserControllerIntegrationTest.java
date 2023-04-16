package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gamesage.store.domain.model.Card;
import com.gamesage.store.domain.model.ResponseError;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.paymentapi.PaymentRequest;
import com.gamesage.store.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Rollback(true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerIntegrationTest {

    public static final String TOKEN_HEADER_NAME = "X-Auth-Token";
    private static final String API_USER_ENDPOINT = "/users";
    private static final String TOPUP_ENDPOINT = "/users/{userId}/topup";

    private User savedUser;
    private String userJson;
    private String token;

    @Value("classpath:request/user/test.json")
    private Resource userJsonResource;
    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setup() throws Exception {
        userJson = Files.readString(Path.of(userJsonResource.getURI()));
        User user = objectMapper.readValue(userJson, User.class);
        userService.deleteAll();
        savedUser = userService.createOne(user);
        token = loginAndGetToken();
        objectMapper
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
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
                .andExpect((jsonPath("$.*.login", Matchers.containsInAnyOrder(savedUser.getLogin(), secondSavedUser.getLogin()))))
                .andExpect((jsonPath("$.*.id", Matchers.containsInAnyOrder(savedUser.getId(), secondSavedUser.getId()))));
    }

    @Test
    void givenUserWithoutToken_whenFindAllUsers_then403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void givenUnauthorizedUser_whenFindAllUsers_then401() throws Exception {
        String wrongToken = "fijwofosk";
        mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TOKEN_HEADER_NAME, wrongToken))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void createOne_thenSuccess() throws Exception {
        //  userService.removeUserByLogin(savedUser.getLogin());
        mockMvc.perform(MockMvcRequestBuilders.post(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.balance").value(savedUser.getBalance()))
                .andExpect(jsonPath("$.login").value(savedUser.getLogin()))
                .andExpect(jsonPath("$.tier.level").value(savedUser.getTier().getLevel()))
                .andExpect(jsonPath("$.tier.id").value(savedUser.getTier().getId()))
                .andExpect(jsonPath("$.tier.cashbackPercentage").value(savedUser.getTier().getCashbackPercentage()));
    }

    @Test
    void givenUser_whenTryTopUp_thenTopupFails() throws Exception {
        Card cardWithWrongNumber = new Card(156L,
                "Jack Black",
                LocalDate.of(2025, 3, 30),
                111);
        PaymentRequest errorPaymentRequest = new PaymentRequest(BigDecimal.ONE, cardWithWrongNumber);

        mockMvc.perform(MockMvcRequestBuilders.post(TOPUP_ENDPOINT, savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(errorPaymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
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
        mockMvc.perform(MockMvcRequestBuilders.post(TOPUP_ENDPOINT, savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(okPaymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.transactionId").isNotEmpty())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseError").isEmpty());
    }

    private String loginAndGetToken() throws Exception {
        String loginEndpoint = "/login";
        return mockMvc.perform(post(loginEndpoint)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(TOKEN_HEADER_NAME);
    }
}

