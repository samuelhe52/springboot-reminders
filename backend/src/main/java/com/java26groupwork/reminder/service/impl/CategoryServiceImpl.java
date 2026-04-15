package com.java26groupwork.reminder.service.impl;

import com.java26groupwork.reminder.entity.TodoCategory;
import com.java26groupwork.reminder.mapper.TodoCategoryMapper;
import com.java26groupwork.reminder.mapper.TodoMapper;
import com.java26groupwork.reminder.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final TodoCategoryMapper categoryMapper;
    private final TodoMapper todoMapper;

    public CategoryServiceImpl(TodoCategoryMapper categoryMapper, TodoMapper todoMapper) {
        this.categoryMapper = categoryMapper;
        this.todoMapper = todoMapper;
    }

    @Override
    public void add(Long userId, TodoCategory category) {
        validateName(category);
        category.setUserId(userId);
        category.setCreateTime(LocalDateTime.now());
        categoryMapper.insert(category);
    }

    @Override
    public List<TodoCategory> list(Long userId) {
        return categoryMapper.listByUserId(userId);
    }

    @Override
    public void update(Long userId, TodoCategory category) {
        if (category == null || category.getId() == null) {
            throw new RuntimeException("Category id is required");
        }
        validateName(category);
        int rows = categoryMapper.updateName(category.getId(), userId, category.getName());
        if (rows == 0) {
            throw new RuntimeException("Category not found");
        }
    }

    @Override
    @Transactional
    public void delete(Long userId, Long categoryId) {
        if (categoryId == null) {
            throw new RuntimeException("Category id is required");
        }
        todoMapper.clearCategoryByCategoryId(categoryId, userId);
        int rows = categoryMapper.deleteByIdAndUserId(categoryId, userId);
        if (rows == 0) {
            throw new RuntimeException("Category not found");
        }
    }

    private void validateName(TodoCategory category) {
        if (category == null || Objects.isNull(category.getName()) || category.getName().trim().isEmpty()) {
            throw new RuntimeException("Category name is required");
        }
    }
}
