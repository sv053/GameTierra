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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@Tag("IntegrationTest")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = "server.port=8083")
@TestPropertySource(locations = "classpath:application.properties")
@SpringJUnitConfig
@AutoConfigureMockMvc
class UserControllerTest {

    private static final String API_ENDPOINT = "/users";
    @Autowired
    private UserService userService;
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    // should work but it doesn't
    @Test
    public void givenUnauthorizedUser_whenFindAllUsers_thenUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    // shouldn't work but it does
    // startIndex here because there is no option to remove all users from another tests from userService
    @Test
    public void givenAuthorizedUser_whenFindAllUsers_thenSuccess() throws Exception {
        User firstUserToAdd = new User(1, "first", "firstkey", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User secondUserToAdd = new User(2, "second", "secondkey", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);

        int startIndex = userService.findAll().size();

        userService.createOne(firstUserToAdd);
        userService.createOne(secondUserToAdd);

        mockMvc.perform(MockMvcRequestBuilders.get(API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", hasSize(userService.findAll().size())))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$[" + startIndex + "].login").value(containsString(firstUserToAdd.getLogin())))
                .andExpect(jsonPath("$[" + (startIndex + 1) + "].login").value(containsString(secondUserToAdd.getLogin())));
    }

    @Test
    public void givenUser_whenCreateOne_thenSuccess() throws Exception {
        User userToAdd = new User(1, "newbee", "letmein", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);

        String userJson = new ObjectMapper().writeValueAsString(userToAdd);

        mockMvc.perform(MockMvcRequestBuilders.post(API_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", isA(LinkedHashMap.class)))
                .andExpect(jsonPath("$", aMapWithSize(6)))
                .andExpect(jsonPath("$.login").exists())
                .andExpect(jsonPath("$.login", notNullValue()))
                .andExpect(jsonPath("$.login").value(userToAdd.getLogin()));
    }

    @Test
    public void givenExistingUser_whenTryTopUp_thenEntityNotFound() throws Exception {

        Card card = new Card(156l,
                "Jack Black",
                LocalDate.now().plusDays(1L),
                111);
        PaymentRequest paymentRequest = new PaymentRequest(BigDecimal.ONE, card);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        mockMvc.perform(MockMvcRequestBuilders.post(API_ENDPOINT + "/2/topup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void givenAuthorizedUser_whenTryTopUp_thenSuccess() throws Exception {
        User userWithRightCreds = new User(1, "admin", "letmein", new Tier(
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

        mockMvc.perform(MockMvcRequestBuilders.post(API_ENDPOINT + "/{id}/topup", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

