package com.java26groupwork.reminder.service.impl;

import com.java26groupwork.reminder.entity.User;
import com.java26groupwork.reminder.entity.TodoCategory;
import com.java26groupwork.reminder.mapper.TodoCategoryMapper;
import com.java26groupwork.reminder.mapper.UserMapper;
import com.java26groupwork.reminder.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private static final List<String> DEFAULT_CATEGORIES = List.of("Default", "Work", "Personal", "Study", "Shopping");

    private final UserMapper userMapper;
    private final TodoCategoryMapper categoryMapper;

    public UserServiceImpl(UserMapper userMapper, TodoCategoryMapper categoryMapper) {
        this.userMapper = userMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public void register(User user) {
        validateUser(user);
        if (userMapper.findByUsername(user.getUsername()) != null) {
            throw new RuntimeException("Username already exists");
        }
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);
        createDefaultCategories(user.getId());
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

    private void createDefaultCategories(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        for (String name : DEFAULT_CATEGORIES) {
            TodoCategory category = new TodoCategory();
            category.setUserId(userId);
            category.setName(name);
            category.setCreateTime(now);
            categoryMapper.insert(category);
        }
    }

    private boolean isBlank(String value) {
        return Objects.isNull(value) || value.trim().isEmpty();
    }
}
