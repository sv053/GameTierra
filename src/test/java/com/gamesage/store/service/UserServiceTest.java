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
    private final UserService userService = new UserService(repository);

    @Test
    void findByLogin_Success_RightUserIsFound() {
        Integer userId = 5;
        User user = new User(userId, null, null, null);

        when(repository.findById(userId)).thenReturn(Optional.of(user));

        assertEquals(user, userService.findById(userId));
    }

    @Test
    void findByLogin_Fail_UserNotFound_Exception() {
        Integer userId = 2;
        when(repository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.findById(userId));
    }
}

