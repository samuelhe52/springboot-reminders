package com.java26groupwork.reminder.controller;

import com.java26groupwork.reminder.entity.Todo;
import com.java26groupwork.reminder.service.TodoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todo")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping("/add")
    public Map<String, String> add(@RequestBody Todo todo, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        todoService.add(userId, todo);
        return Map.of("msg", "ok");
    }

    @GetMapping("/list")
    public List<Todo> list(@RequestParam(required = false) Integer status,
                           @RequestParam(required = false) Long categoryId,
                           HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return todoService.list(userId, status, categoryId);
    }

    @PostMapping("/update")
    public Map<String, String> update(@RequestBody Todo todo, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        todoService.update(userId, todo);
        return Map.of("msg", "ok");
    }

    @PostMapping("/delete")
    public Map<String, String> delete(@RequestBody Todo todo, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        todoService.delete(userId, todo.getId());
        return Map.of("msg", "ok");
    }
}
