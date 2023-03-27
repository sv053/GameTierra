package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.gamesage.store.domain.model.Card;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.paymentapi.PaymentRequest;
import com.gamesage.store.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class UserControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);
    private static final String API_USER_ENDPOINT = "/users";
    private static final String API_LOGIN_ENDPOINT = "/login";

    @Autowired
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String userJson;

    @BeforeEach
    void setup() {
        userJson = "{\"id\": 2, " +
                " \"login\": \"admin\", " +
                " \"password\": \"letmein\", " +
                " \"tier\": { " +
                " \"cashbackPercentage\": 0.0, " +
                " \"id\": 1, " +
                " \"level\": \"FREE\" " +
                "}, " +
                "\"balance\": 815.16, " +
                "\"games\": []}";
    }

    @Test
    void givenAuthorizedUser_whenFindAllUsers_thenSuccess() throws Exception {
        User firstUserToAdd = new User(null, "admin", "letmein", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User secondUserToAdd = new User(null, "second", "secondPass", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);

        User firstSavedUser = userService.createOne(firstUserToAdd);
        User secondSavedUser = userService.createOne(secondUserToAdd);

        MockHttpServletResponse response = mockMvc.perform(post(API_LOGIN_ENDPOINT)
                        .content(userJson)
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Auth-Token", response.getHeader("X-Auth-Token")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(userService.findAll().size())))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$[0].login").value(containsString(firstSavedUser.getLogin())))
                .andExpect(jsonPath("$[1].login").value(containsString(secondSavedUser.getLogin())));
    }

    @Test
    void givenUserWithoutToken_whenFindAllUsers_then403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void givenUnauthorizedUser_whenFindAllUsers_then401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Auth-Token", "11111"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void createOne_thenSuccess() throws Exception {
        User user = new User(888, "admin", "multipass", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);

        mockMvc.perform(MockMvcRequestBuilders.post(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(5)))
                .andExpect(jsonPath("$.login").exists())
                .andExpect(jsonPath("$.login", notNullValue()))
                .andExpect(jsonPath("$.login").value(user.getLogin()));
    }

    @Test
    void givenExistingUser_whenTryTopUp_thenEntityNotFound() throws Exception {
        Card card = new Card(156l,
                "Jack Black",
                LocalDate.now().plusDays(1L),
                111);
        PaymentRequest paymentRequest = new PaymentRequest(BigDecimal.ONE, card);

        objectMapper.registerModule(new JSR310Module());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        mockMvc.perform(MockMvcRequestBuilders.post(API_USER_ENDPOINT + "/2/topup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenUser_whenTryTopUp_thenSuccess() throws Exception {
        User userWithRightCreds = new User(1, "admin", "letmein", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        userService.createOne(userWithRightCreds);

        Card card = new Card(156l,
                "Jack Black",
                LocalDate.now().plusDays(1L),
                111);
        PaymentRequest paymentRequest = new PaymentRequest(BigDecimal.ONE, card);
        objectMapper.registerModule(new JSR310Module());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        mockMvc.perform(MockMvcRequestBuilders.post(API_USER_ENDPOINT + "/{id}/topup", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

