package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:application-test.properties")
class TokenCleanupServiceTest {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private TokenCleanupService tokenCleanupService;
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private ScheduledTaskHolder scheduledTaskHolder;
    @Value("${com.gamesage.store.cleanup}")
    private String cleanupCronExpression;
    @Value("${com.gamesage.store.tokenExpiryInterval}")
    private int tokenExpiryInterval;


    @Test
    void removeExpiredTokensScheduler_Success() {
        Set<ScheduledTask> scheduledTasks = scheduledTaskHolder.getScheduledTasks();
        var task = scheduledTasks.stream()
            .filter(scheduledTask -> scheduledTask.getTask() instanceof CronTask)
            .map(scheduledTask -> (CronTask) scheduledTask.getTask())
            .filter(cronTask -> isTaskForMethod(cronTask, "removeExpiredTokens"))
            .findFirst();
        String foundTokenValue = task.map(CronTask::getExpression).orElse("");

        assertEquals(cleanupCronExpression, foundTokenValue);
    }

    boolean isTaskForMethod(CronTask cronTask, String methodName) {
        var taskMethod = cronTask.toString();
        return taskMethod.contains(methodName);
    }

    @Test
    void removeExpiredTokens_Success() {
        User user = new User(null, "agamer", "lerida", new Tier(
            3, "SILVER", 10.d), BigDecimal.TEN);
        User savedUser = userService.createOne(user);
        LocalDateTime localDateTime = LocalDateTime.now().minus(tokenExpiryInterval, ChronoUnit.DAYS);
        AuthToken token = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId(), localDateTime);
        tokenService.createToken(token);
        tokenCleanupService.removeExpiredTokens();

        assertEquals(Optional.empty(), tokenService.findTokenById(user.getId()));
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
        tokenCleanupService.removeExpiredTokens();

        String foundTokenAfterRemoving = tokenService.findTokenByUserId(userId)
            .map(AuthToken::getValue).orElse("");

        assertTrue(encoder.matches(token.getValue(), foundTokenAfterRemoving));
    }
}

