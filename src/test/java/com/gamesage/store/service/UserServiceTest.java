package com.gamesage.store.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;
    @InjectMocks
    private UserService userService;

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

