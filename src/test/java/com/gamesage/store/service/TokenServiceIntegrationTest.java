package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.WrongCredentialsException;
import com.gamesage.store.logger.LoggerManager;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource("classpath:application-test.properties")
class TokenServiceIntegrationTest {

    private static final Logger logger = LoggerManager.getLogger();

    private final CountDownLatch latch = new CountDownLatch(1);
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TokenCleanupService tokenCleanupService;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private ScheduledTaskHolder scheduledTaskHolder;
    @Autowired
    private ApplicationContext applicationContext;
    @Value("${com.gamesage.store.cleanup}")
    private String cleanupCronExpression;
    @Value("${com.gamesage.store.tokenExpiryInterval}")
    private int tokenExpiryInterval;

    @Test
    void findByTokenValue_Success() {
        User userWithoutToken = new User(null, "user1", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(userWithoutToken);
        AuthToken tokenToCreate = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now());
        AuthToken tokenToFind = tokenService.createToken(tokenToCreate);
        Optional<AuthToken> foundToken = tokenService.findTokenByUserId(tokenToFind.getUserId());
        String fountTokenValue = "";
        if (foundToken.isEmpty()) {
            logger.error("#findByTokenValue_Success foundToken is empty");
        } else {
            fountTokenValue = foundToken.get().getValue();
        }
        assertNotNull(foundToken);
        assertTrue(encoder.matches(tokenToCreate.getValue(), fountTokenValue));
    }

    @Test
    void findByUserId_Success() {
        User userWithoutToken = new User(null, "user111", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(userWithoutToken);
        AuthToken token = new AuthToken(1, "ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now());
        tokenService.createToken(token);
        Optional<AuthToken> foundToken = tokenService.findTokenByUserId(savedUser.getId());
        String foundTokenValue = "";
        if (foundToken.isPresent()) {
            foundTokenValue = foundToken.get().getValue();
        }

        assertTrue(foundToken.isPresent());
        assertTrue(encoder.matches(token.getValue(), foundTokenValue));
    }

    @Test
    void findByUserId_WrongId_EmptyResult() {
        assertEquals(Optional.empty(), tokenService.findTokenByUserId(888888888));
    }

    @Test
    void findByUserId_lessThanZeroId_Failure_Exception() {
        assertThrows(WrongCredentialsException.class, () -> tokenService.findTokenByUserId(-1));
    }

    @Test
    void saveTokenValue_Success() {
        User user = new User(null, "agamer", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(user);
        AuthToken token = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now());
        tokenService.createToken(token);
        Optional<AuthToken> foundToken = tokenService.findTokenByUserId(savedUser.getId());
        String foundTokenValue = "";
        if (foundToken.isPresent()) {
            foundTokenValue = foundToken.get().getValue();
        }

        assertTrue(encoder.matches(token.getValue(), foundTokenValue));
    }


    @Test
    void removeExpiredTokensScheduler_Success() {
        Set<ScheduledTask> scheduledTasks1 = scheduledTaskHolder.getScheduledTasks();
        long count = scheduledTasks1.stream()
                .filter(scheduledTask -> scheduledTask.getTask() instanceof CronTask)
                .map(scheduledTask -> (CronTask) scheduledTask.getTask())
                .filter(cronTask -> cronTask.getExpression().equals(cleanupCronExpression))
                .count();
        assertThat(count).isEqualTo(1L);
    }

    @Test
    public void expiredTokenRemoveScheduler() throws InterruptedException {
        User user = new User(null, "agamer", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(user);
        AuthToken token = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now().minus(100, ChronoUnit.DAYS));
        AuthToken createdToken = tokenService.createToken(token);
        logger.info("#expiredTokenRemoveScheduler foundToken has been created " + createdToken);

        Optional<AuthToken> foundToken = tokenService.findTokenByUserId(savedUser.getId());
        String foundTokenValue = "";
        if (foundToken.isPresent()) {
            foundTokenValue = foundToken.get().getValue();
        } else {
            logger.error("#expiredTokenRemoveScheduler foundToken is empty");
        }
        assertTrue(encoder.matches(token.getValue(), foundTokenValue));

        taskScheduler.schedule(() -> {
            tokenCleanupService.removeExpiredTokens();
            latch.countDown();
        }, new Date(System.currentTimeMillis() + 5000));

        assertTrue(latch.await(10, TimeUnit.SECONDS));

        assertEquals(Optional.empty(), tokenService.findTokenById(0));
        logger.info("#expiredTokenRemoveScheduler foundToken has been removed " + tokenService.findTokenById(0));
    }

    @Test
    void removeExpiredTokens_Success() {
        User user = new User(null, "agamer", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(user);
        LocalDateTime localDateTime = LocalDateTime.now().minus(tokenExpiryInterval, ChronoUnit.DAYS);

        AuthToken token = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId(), localDateTime);
        tokenService.createToken(token);
        Optional<AuthToken> foundToken = tokenService.findTokenByUserId(savedUser.getId());
        String foundTokenValue = "";
        if (foundToken.isPresent()) {
            foundTokenValue = foundToken.get().getValue();
        }
        assertNotNull(tokenService.findTokenById(0));
        assertFalse(foundTokenValue.isBlank());

        assertTrue(encoder.matches(token.getValue(), foundTokenValue));

        tokenCleanupService.removeExpiredTokens();

        assertEquals(Optional.empty(), tokenService.findTokenById(0));
    }

    @Test
    public void expiredTokenRemoveScheduler_tooEarlyToRemoveToken() {
        User user = new User(null, "agamer", "lerida", new Tier(
                3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(user);
        int userId = savedUser.getId();
        LocalDateTime localDateTime = LocalDateTime.now().plus(tokenExpiryInterval, ChronoUnit.DAYS);
        AuthToken token = new AuthToken("ftyzrdtcfjyiuh", userId, localDateTime);
        tokenService.createToken(token);
        Optional<AuthToken> foundTokenBeforeRemoving = tokenService.findTokenByUserId(userId);
        String foundTokenValueBeforeRemoving = "";
        if (foundTokenBeforeRemoving.isPresent()) {
            foundTokenValueBeforeRemoving = foundTokenBeforeRemoving.get().getValue();
        }
        assertFalse(foundTokenValueBeforeRemoving.isBlank());
        assertTrue(encoder.matches(token.getValue(), foundTokenValueBeforeRemoving));

        tokenCleanupService.removeExpiredTokens();

        Optional<AuthToken> foundTokenAfterRemoving = tokenService.findTokenByUserId(userId);

        assertNotNull(foundTokenAfterRemoving);

        String foundTokenValueAfterRemoving = "";
        if (foundTokenBeforeRemoving.isPresent()) {
            foundTokenValueAfterRemoving = foundTokenBeforeRemoving.get().getValue();
        }
        assertTrue(encoder.matches(token.getValue(), foundTokenValueAfterRemoving));
    }
}

