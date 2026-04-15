package com.java26groupwork.reminder.service.impl;

import com.java26groupwork.reminder.entity.User;
import com.java26groupwork.reminder.mapper.UserMapper;
import com.java26groupwork.reminder.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public void register(User user) {
        validateUser(user);
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
    }

    @Override
    public User login(User user) {
        validateUser(user);
        User dbUser = userMapper.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (dbUser == null) {
            throw new RuntimeException("Invalid username or password");
        }
        return dbUser;
    }

    private void validateUser(User user) {
        if (user == null || isBlank(user.getUsername()) || isBlank(user.getPassword())) {
            throw new RuntimeException("Username and password are required");
        }
    }

    private boolean isBlank(String value) {
        return Objects.isNull(value) || value.trim().isEmpty();
    }
}
