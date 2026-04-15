package com.java26groupwork.reminder.controller;

import com.java26groupwork.reminder.entity.TodoCategory;
import com.java26groupwork.reminder.service.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public Map<String, String> add(@RequestBody TodoCategory category, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        categoryService.add(userId, category);
        return Map.of("msg", "ok");
    }

    @GetMapping("/list")
    public List<TodoCategory> list(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return categoryService.list(userId);
    }

    @PostMapping("/update")
    public Map<String, String> update(@RequestBody TodoCategory category, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        categoryService.update(userId, category);
        return Map.of("msg", "ok");
    }

    @PostMapping("/delete")
    public Map<String, String> delete(@RequestBody TodoCategory category, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        categoryService.delete(userId, category.getId());
        return Map.of("msg", "ok");
    }
}
