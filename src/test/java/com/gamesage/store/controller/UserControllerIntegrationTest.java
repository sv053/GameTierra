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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerIntegrationTest {

    public static final String TOKEN_HEADER_NAME = "X-Auth-Token";
    private static final String API_USER_ENDPOINT = "/users";
    private static final String TOPUP_ENDPOINT = "/users/{userId}/topup";
    private static final String USER_JSON_FILE_PATH = "src/test/resources/user.json";
    private static final String NEW_USER_JSON_FILE_PATH = "src/test/resources/notSavedUser.json";

    private User user;
    private User savedUser;
    private String userJson;
    private String token;

    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setup() throws Exception {
        userJson = Files.readString(Path.of(USER_JSON_FILE_PATH));
        user = objectMapper.readValue(userJson, User.class);
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
        List<User> savedUsers = List.of(savedUser, secondSavedUser);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TOKEN_HEADER_NAME, token))
                .andExpect(status().isOk());

        for (int i = 0; i < savedUsers.size(); i++) {
            User user = savedUsers.get(i);
            String jsonPathPrefix = String.format("$[%d].", i);
            resultActions.andExpect(jsonPath(jsonPathPrefix + "id").value(user.getId()));
        }
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
        String newUserJson = Files.readString(Path.of(NEW_USER_JSON_FILE_PATH));
        user = objectMapper.readValue(newUserJson, User.class);
        mockMvc.perform(MockMvcRequestBuilders.post(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newUserJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.balance").value(user.getBalance()))
                .andExpect(jsonPath("$.login").value(user.getLogin()))
                .andExpect(jsonPath("$.tier.level").value(user.getTier().getLevel()))
                .andExpect(jsonPath("$.tier.id").value(user.getTier().getId()))
                .andExpect(jsonPath("$.tier.cashbackPercentage").value(user.getTier().getCashbackPercentage()));
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
                .andExpect(jsonPath("$.transactionId").value("cdy-5r3fiy-6ki6-6nbvh8g"))
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
                .andExpect(jsonPath("$.transactionId").value("cdy-5r3fiy-6ki6-6nbvh8g"))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.responseError").isEmpty());
    }

    private String loginAndGetToken() throws Exception {
        String loginEndpoint = "/login";
        savedUser = userService.createOne(user); // should be created before login
        return mockMvc.perform(post(loginEndpoint)
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(TOKEN_HEADER_NAME);
    }
}

