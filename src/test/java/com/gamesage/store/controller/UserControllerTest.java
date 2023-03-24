package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.Card;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.paymentapi.PaymentRequest;
import com.gamesage.store.service.TokenService;
import com.gamesage.store.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
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
    private TokenService tokenService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenAuthorizedUser_whenFindAllUsers_thenSuccess() throws Exception {
        User firstUserToAdd = new User(1, "first", "firstPass", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User secondUserToAdd = new User(2, "second", "secondPass", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);

        userService.createOne(firstUserToAdd);
        userService.createOne(secondUserToAdd);

        mockMvc.perform(post(API_LOGIN_ENDPOINT)
                        .content(new ObjectMapper().writeValueAsString(firstUserToAdd))
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Optional<AuthToken> token = tokenService.findTokenByLogin(firstUserToAdd.getLogin());
        String tokenValue = "";
        if (token.isPresent()) {
            tokenValue = token.get().getValue();
        }

        mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Auth-Token", tokenValue))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(userService.findAll().size())))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$[0].login").value(containsString(firstUserToAdd.getLogin())))
                .andExpect(jsonPath("$[1].login").value(containsString(secondUserToAdd.getLogin())));
    }

    @Test
    void givenUnauthorizedUser_whenFindAllUsers_then403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_USER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    void givenUser_whenCreateOne_thenSuccess() throws Exception {
        User user = new User(888, "lilu", "multipass", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(user);
        mockMvc.perform(post(API_LOGIN_ENDPOINT)
                        .content(new ObjectMapper().writeValueAsString(user))
                        .contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        String userJson = new ObjectMapper().writeValueAsString(user);

        Optional<AuthToken> token = tokenService.findTokenByLogin(user.getLogin());

        String tokenValue = "";
        if (token.isPresent()) {
            tokenValue = token.get().getValue();
        }
        mockMvc.perform(MockMvcRequestBuilders.post(API_USER_ENDPOINT)
                        .header("X-Auth-Token", tokenValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
//                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
//                .andExpect(jsonPath("$", aMapWithSize(1)))
//                .andExpect(jsonPath("$.login").exists())
//                .andExpect(jsonPath("$.login", notNullValue()))
//                .andExpect(jsonPath("$.login").value(savedUser.getLogin()));
    }

    @Test
    void givenExistingUser_whenTryTopUp_thenEntityNotFound() throws Exception {
        Card card = new Card(156l,
                "Jack Black",
                LocalDate.now().plusDays(1L),
                111);
        PaymentRequest paymentRequest = new PaymentRequest(BigDecimal.ONE, card);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);


        mockMvc.perform(MockMvcRequestBuilders.post(API_USER_ENDPOINT + "/2/topup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void givenAuthorizedUser_whenTryTopUp_thenSuccess() throws Exception {
        User userWithRightCreds = new User(1, "third", "letmein", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        userService.createOne(userWithRightCreds);

        Card card = new Card(156l,
                "Jack Black",
                LocalDate.now().plusDays(1L),
                111);
        PaymentRequest paymentRequest = new PaymentRequest(BigDecimal.ONE, card);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        mockMvc.perform(MockMvcRequestBuilders.post(API_USER_ENDPOINT + "/{id}/topup", 1)
                        .header("Authorization", "Basic " + Base64.getEncoder().encodeToString(("@Test").getBytes()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

