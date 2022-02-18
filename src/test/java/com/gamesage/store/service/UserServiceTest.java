package com.gamesage.store.service;

import com.gamesage.store.domain.model.User;
import com.gamesage.store.domain.repository.UserRepository;
import com.gamesage.store.exception.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

//    @Test
//    void findById_Success_RightUserIsFound() {
//        Integer userId = 5;
//        User user = new User(userId, null, null, null);
//
//        when(repository.findById(userId)).thenReturn(Optional.of(user));
//
//        assertEquals(user, userService.findById(userId));
//    }
//
//    @Test
//    void findById_Fail_NotFound_Exception() {
//        Integer userId = 2;
//        when(repository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(EntityNotFoundException.class, () -> userService.findById(userId));
//    }
}

