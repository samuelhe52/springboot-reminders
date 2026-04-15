package com.java26groupwork.reminder.service;

import com.java26groupwork.reminder.entity.TodoCategory;

import java.util.List;

public interface CategoryService {
    void add(Long userId, TodoCategory category);

    List<TodoCategory> list(Long userId);

    void update(Long userId, TodoCategory category);

    void delete(Long userId, Long categoryId);
}
