package com.gamesage.store.service;

import com.gamesage.store.domain.model.AuthToken;
import com.gamesage.store.domain.model.Tier;
import com.gamesage.store.domain.model.User;
import com.gamesage.store.exception.WrongCredentialsException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.scheduling.config.ScheduledTaskHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
//import static org.awaitility.Awaitility.*;
//import static org.awaitility.Duration.*;


@SpringBootTest
@Transactional
@PropertySource("classpath:application.properties")
class TokenServiceIntegrationTest {

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

	@Test
	void findByTokenValue_Success() {
		User userWithoutToken = new User(null, "user1", "lerida", new Tier(
				3, "SILVER", 10.d), BigDecimal.TEN);
		User savedUser = userService.createOne(userWithoutToken);
		AuthToken tokenToCreate = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now());
		AuthToken tokenToFind = tokenService.createToken(tokenToCreate);
		AuthToken foundToken = tokenService.findTokenByUserId(tokenToFind.getUserId()).get();

		assertTrue(encoder.matches(tokenToCreate.getValue(), foundToken.getValue()));
	}

	@Test
	void findByLogin_Success() {
		User userWithoutToken = new User(null, "user111", "lerida", new Tier(
				3, "SILVER", 10.d), BigDecimal.TEN);
		User savedUser = userService.createOne(userWithoutToken);
		AuthToken token = new AuthToken(1, "ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now());
		tokenService.createToken(token);
		Optional<AuthToken> foundToken = tokenService.findTokenById(savedUser.getId());
		String foundTokenValue = "";
		if (foundToken.isPresent()) {
			foundTokenValue = foundToken.get().getValue();
		}

		assertTrue(foundToken.isPresent());
		assertTrue(encoder.matches(token.getValue(), foundTokenValue));
	}

	//@Test
	void findByLogin_Failure_Exception() {
		assertThrows(WrongCredentialsException.class, () -> tokenService.findTokenByUserId(88888888));
	}

	@Test
	void saveTokenValue_Success() {
		User user = new User(null, "agamer", "lerida", new Tier(
				3, "SILVER", 10.d), BigDecimal.TEN);
		User savedUser = userService.createOne(user);
		AuthToken token = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now());
		tokenService.createToken(token);
		AuthToken foundToken = (tokenService.findTokenById(savedUser.getId()).get());

		assertTrue(encoder.matches(token.getValue(), foundToken.getValue()));
	}

	@Test
	void removeExpiredTokens_Success() {
		User user = new User(null, "agamer", "lerida", new Tier(
				3, "SILVER", 10.d), BigDecimal.TEN);
		User savedUser = userService.createOne(user);
		AuthToken token = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now().minus(100, ChronoUnit.DAYS));
		AuthToken savedToken = tokenService.createToken(token);
		AuthToken foundToken = (tokenService.findTokenById(savedUser.getId())).get();

		assertTrue(encoder.matches(token.getValue(), foundToken.getValue()));

		tokenCleanupService.removeExpiredTokens();

		assertEquals(Optional.empty(), tokenService.findTokenById(0));
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

//	@Test
//	void testScheduledTasks() {
//		await().atMost(10, TimeUnit.SECONDS)
//				.until(() -> {
//					String[] taskBeanNames = applicationContext.getBeanNamesForAnnotation(Scheduled.class);
//					for (String beanName : taskBeanNames) {
//						Object bean = applicationContext.getBean(beanName);
//						if (bean instanceof Runnable) {
//							Scheduled scheduledAnnotation = applicationContext.findAnnotationOnBean(beanName, Scheduled.class);
//							if (scheduledAnnotation != null && scheduledAnnotation.cron().equals(cleanupCronExpression)) {
//								return true;
//							}
//						}
//					}
//					return false;
//				});
//
//		assertThat(taskFound).isTrue();
//	}

	@Test
	public void expiredTokenRemoveScheduler() throws InterruptedException {
		User user = new User(null, "agamer", "lerida", new Tier(
				3, "SILVER", 10.d), BigDecimal.TEN);
		User savedUser = userService.createOne(user);
		AuthToken token = new AuthToken("ftyzrdtcfjyiuh", savedUser.getId(), LocalDateTime.now().minus(100, ChronoUnit.DAYS));
		AuthToken savedToken = tokenService.createToken(token);
		AuthToken foundToken = (tokenService.findTokenById(savedUser.getId())).get();

		assertTrue(encoder.matches(token.getValue(), foundToken.getValue()));

		taskScheduler.schedule(() -> {
			tokenCleanupService.removeExpiredTokens();
			latch.countDown();
		}, new Date(System.currentTimeMillis() + 5000));

		assertTrue(latch.await(10, TimeUnit.SECONDS));

		assertEquals(Optional.empty(), tokenService.findTokenById(0));
	}
}

