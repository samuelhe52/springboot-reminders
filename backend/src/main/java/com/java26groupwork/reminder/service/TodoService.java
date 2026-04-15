package com.java26groupwork.reminder.service;

import com.java26groupwork.reminder.entity.Todo;

import java.util.List;

public interface TodoService {
    void add(Long userId, Todo todo);

    List<Todo> list(Long userId, Integer status, Long categoryId);

    void update(Long userId, Todo todo);

    void delete(Long userId, Long id);
}
