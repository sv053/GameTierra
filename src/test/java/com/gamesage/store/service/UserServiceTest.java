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

    @Test
    void findByLogin_UserExists_User() {
        final UserRepository repository = mock(UserRepository.class);
        final String userLogin = "prima";
        final User user = new User(userLogin, null, null);

        when(repository.findBy(userLogin)).thenReturn(Optional.of(user));

        final UserService userService = new UserService(repository);

        assertEquals(user, userService.findByLogin(userLogin));
    }

    @Test
    void findByLogin_UserDoesntExist_IAE() {
        final UserRepository repository = mock(UserRepository.class);
        final String userLogin = "primo";
        when(repository.findBy(userLogin)).thenReturn(Optional.empty());

        final UserService userService = new UserService(repository);

        assertThrows(IllegalArgumentException.class, () -> userService.findByLogin(userLogin));
    }
}

