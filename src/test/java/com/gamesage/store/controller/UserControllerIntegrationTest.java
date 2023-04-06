package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.gamesage.store.domain.model.Card;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.paymentapi.PaymentRequest;
import com.gamesage.store.service.UserService;
import org.junit.jupiter.api.Test;
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
import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerIntegrationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final Tier tier = new Tier(5, "PLATINUM", 30.0);
    private final User user = new User(1, "testuser", "testpass", tier,
            BigDecimal.valueOf(1000));
    private final String USER_JSON = user.toString();
    private final String TOKEN_HEADER_NAME = "X-Auth-Token";
    private final String API_USER_ENDPOINT = "/users";
    private final Card card = new Card(156L,
            "Jack Black",
            LocalDate.of(2025, 3, 30),
            111);
    private final PaymentRequest paymentRequest = new PaymentRequest(BigDecimal.ONE, card);


    @Test
    void givenAuthorizedUser_whenFindAllUsers_thenSuccess() throws Exception {
        User savedUser = userService.createOne(user);
        User secondUser = new User(null, "second", "secondPass", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User secondSavedUser = userService.createOne(secondUser);
        List<User> savedUsers = List.of(savedUser, secondSavedUser);

        String token = loginAndGetToken();

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TOKEN_HEADER_NAME, token))
                .andExpect(status().isOk()
                );

        for (int i = 0; i < savedUsers.size(); i++) {
            User user = savedUsers.get(i);
            String jsonPathPrefix = String.format("$[%d].", i);
            resultActions.andExpect(jsonPath(jsonPathPrefix + "login").value(user.getLogin()));
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
        String noToken = "fijwofosk";
        mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TOKEN_HEADER_NAME, noToken))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void createOne_thenSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(USER_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.login").value(user.getLogin()));
    }

    @Test
    void givenExistingUser_whenTryTopUp_thenEntityNotFound() throws Exception {
        configureDateMapper();
        mockMvc.perform(MockMvcRequestBuilders.post(API_USER_ENDPOINT + "/999/topup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenUser_whenTryTopUp_thenSuccess() throws Exception {
        User savedUser = userService.createOne(user);
        configureDateMapper();

        mockMvc.perform(MockMvcRequestBuilders.post(API_USER_ENDPOINT + "/{id}/topup", savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private void configureDateMapper() {
        objectMapper
                .registerModule(new JSR310Module())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    }

    private String loginAndGetToken() throws Exception {
        String loginEndpoint = "/login";
        return mockMvc.perform(post(loginEndpoint)
                        .content(USER_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(TOKEN_HEADER_NAME);
    }
}

