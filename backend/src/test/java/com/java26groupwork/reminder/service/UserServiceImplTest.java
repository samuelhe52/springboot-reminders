package com.java26groupwork.reminder.service;

import com.java26groupwork.reminder.entity.User;
import com.java26groupwork.reminder.mapper.UserMapper;
import com.java26groupwork.reminder.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void registerShouldThrowWhenUsernameAlreadyExists() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("123456");

        when(userMapper.findByUsername("alice")).thenReturn(new User());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.register(user));

        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void registerShouldSetCreateTimeAndInsertUser() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("123456");

        userService.register(user);

        assertNotNull(user.getCreateTime());
        verify(userMapper).insert(user);
    }

    @Test
    void loginShouldThrowWhenUsernameOrPasswordIsWrong() {
        User user = new User();
        user.setUsername("alice");
        user.setPassword("bad-password");

        when(userMapper.findByUsernameAndPassword("alice", "bad-password")).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.login(user));

        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void loginShouldReturnUserWhenCredentialsAreCorrect() {
        User request = new User();
        request.setUsername("alice");
        request.setPassword("123456");

        User dbUser = new User();
        dbUser.setId(1L);
        dbUser.setUsername("alice");

        when(userMapper.findByUsernameAndPassword("alice", "123456")).thenReturn(dbUser);

        User result = userService.login(request);

        assertEquals(1L, result.getId());
        assertEquals("alice", result.getUsername());
    }
}
