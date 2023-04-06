package com.gamesage.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesage.store.domain.model.Game;
import com.gamesage.store.domain.model.PurchaseIntent;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.service.GameService;
import com.gamesage.store.service.OrderService;
import com.gamesage.store.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {

    private static final String API_LOGIN_ENDPOINT = "/login";
    private static final String API_ORDER_ENDPOINT = "/cart";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderService orderService;

    private final Tier tier = new Tier(5, "PLATINUM", 30.0);
    private final User user = new User(1, "testuser", "testpass", tier,
            BigDecimal.valueOf(1000));
    private final String USER_JSON = user.toString();
    private final Game GAME = new Game("THE_LAST_OF_US", BigDecimal.valueOf(7.28d));
    private final String WRONG_TOKEN_HEADER = "unknownTokenValue";
    private final String TOKEN_HEADER_TITLE = "X-Auth-Token";
    private final String SLASH = "/";

    @Test
    void givenRightCreds_shouldFindOrderById() throws Exception {
        User savedUser = userService.createOne(user);
        Game savedGame = gameService.createOne(GAME);
        int orderId = 2;

        String tokenValue = loginAndGetToken();

        PurchaseIntent intent = orderService.buyGame(savedGame.getId(), savedUser.getId());

        assert (intent.getMessage().equals(PurchaseIntent.PurchaseMessage.PURCHASE_SUCCESSFUL));

        mockMvc.perform(get(API_ORDER_ENDPOINT + SLASH + orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(TOKEN_HEADER_TITLE, tokenValue))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.login").value(savedUser.getLogin()));
    }

    @Test
    void givenWrongCreds_shouldNotFindOrderByIdAndReturn401() throws Exception {
        int wrongId = -15;
        mockMvc.perform(get(API_ORDER_ENDPOINT + SLASH + wrongId)
                        .header(TOKEN_HEADER_TITLE, WRONG_TOKEN_HEADER)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void givenUnknownUser_shouldNotFindOrderByIdAndReturn403() throws Exception {
        int wrongId = -15;
        mockMvc.perform(get(API_ORDER_ENDPOINT + SLASH + wrongId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void givenRightCreds_shouldFindAllOrders() throws Exception {
        User savedUser = userService.createOne(user);
        Game savedGame = gameService.createOne(GAME);
        String tokenValue = loginAndGetToken();

        orderService.buyGame(savedGame.getId(), savedUser.getId());

        mockMvc.perform(get(API_ORDER_ENDPOINT)
                        .header(TOKEN_HEADER_TITLE, tokenValue)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenRightCreds_shouldBuyGame() throws Exception {
        User user = objectMapper.readValue(USER_JSON, User.class);
        User savedUser = userService.createOne(user);
        Game savedGame = gameService.createOne(GAME);
        String tokenValue = loginAndGetToken();

        mockMvc.perform(post(API_ORDER_ENDPOINT + "/{gameId}/{userId}",
                        savedGame.getId(), savedUser.getId())
                        .header(TOKEN_HEADER_TITLE, tokenValue)
                        .content(USER_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenWrongCreds_shouldNotBuyGame() throws Exception {
        User savedUser = userService.createOne(user);
        Game savedGame = gameService.createOne(GAME);

        mockMvc.perform(post(API_ORDER_ENDPOINT + "/{gameId}/{userId}",
                        savedGame.getId(), savedUser.getId())
                        .header(TOKEN_HEADER_TITLE, WRONG_TOKEN_HEADER)
                        .content(USER_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    private String loginAndGetToken() throws Exception {
        return mockMvc.perform(post(API_LOGIN_ENDPOINT)
                        .content(USER_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(TOKEN_HEADER_TITLE);
    }
}

