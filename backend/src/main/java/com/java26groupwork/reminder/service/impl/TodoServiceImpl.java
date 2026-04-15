package com.java26groupwork.reminder.service.impl;

import com.java26groupwork.reminder.entity.Todo;
import com.java26groupwork.reminder.entity.TodoCategory;
import com.java26groupwork.reminder.mapper.TodoCategoryMapper;
import com.java26groupwork.reminder.mapper.TodoMapper;
import com.java26groupwork.reminder.service.TodoService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class TodoServiceImpl implements TodoService {

    private static final String DEFAULT_CATEGORY_NAME = "Default";

    private final TodoMapper todoMapper;
    private final TodoCategoryMapper categoryMapper;

    public TodoServiceImpl(TodoMapper todoMapper, TodoCategoryMapper categoryMapper) {
        this.todoMapper = todoMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public void add(Long userId, Todo todo) {
        applyDefaultCategoryIfMissing(userId, todo);
        validateTodo(todo, userId);
        todo.setUserId(userId);
        todo.setStatus(0);
        todo.setCreateTime(LocalDateTime.now());
        todo.setUpdateTime(LocalDateTime.now());
        todoMapper.insert(todo);
    }

    @Override
    public List<Todo> list(Long userId, Integer status, Long categoryId) {
        return todoMapper.listByFilter(userId, status, categoryId);
    }

    @Override
    public void update(Long userId, Todo todo) {
        if (todo == null || todo.getId() == null) {
            throw new RuntimeException("Todo id is required");
        }
        validateTodo(todo, userId);
        todo.setUserId(userId);
        todo.setUpdateTime(LocalDateTime.now());
        int rows = todoMapper.updateByOwner(todo);
        if (rows == 0) {
            throw new RuntimeException("Todo not found");
        }
    }

    @Override
    public void delete(Long userId, Long id) {
        if (id == null) {
            throw new RuntimeException("Todo id is required");
        }
        int rows = todoMapper.deleteByIdAndUserId(id, userId);
        if (rows == 0) {
            throw new RuntimeException("Todo not found");
        }
    }

    private void validateTodo(Todo todo, Long userId) {
        if (todo == null || isBlank(todo.getTitle())) {
            throw new RuntimeException("Todo title is required");
        }
        if (todo.getStatus() == null) {
            todo.setStatus(0);
        }
        if (todo.getStatus() != 0 && todo.getStatus() != 1) {
            throw new RuntimeException("Invalid todo status");
        }
        if (todo.getCategoryId() != null && categoryMapper.countByIdAndUserId(todo.getCategoryId(), userId) == 0) {
            throw new RuntimeException("Category not found");
        }
    }

    private void applyDefaultCategoryIfMissing(Long userId, Todo todo) {
        if (todo == null || todo.getCategoryId() != null) {
            return;
        }
        Long defaultCategoryId = categoryMapper.findIdByUserIdAndName(userId, DEFAULT_CATEGORY_NAME);
        if (defaultCategoryId == null) {
            TodoCategory category = new TodoCategory();
            category.setUserId(userId);
            category.setName(DEFAULT_CATEGORY_NAME);
            category.setCreateTime(LocalDateTime.now());
            categoryMapper.insert(category);
            defaultCategoryId = category.getId();
        }
        todo.setCategoryId(defaultCategoryId);
    }

    private boolean isBlank(String value) {
        return Objects.isNull(value) || value.trim().isEmpty();
    }
}
