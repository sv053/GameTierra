package com.gamesage.store.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceTest {

    private final UserRepository repository = mock(UserRepository.class);

    @Test
    void findByLogin_UserFound() {
        final Integer userId = 5;
        User user = new User(userId, null, null, null);

        when(this.repository.findById(userId)).thenReturn(Optional.of(user));

        UserService userService = new UserService(this.repository);

        assertEquals(user, userService.findById(userId));
    }

    @Test
    void findByLogin_UserNotFound_Exception() {
        final Integer userId = 2;
        when(this.repository.findById(userId)).thenReturn(Optional.empty());

        UserService userService = new UserService(this.repository);

        assertThrows(IllegalArgumentException.class, () -> userService.findById(userId));
    }
}

