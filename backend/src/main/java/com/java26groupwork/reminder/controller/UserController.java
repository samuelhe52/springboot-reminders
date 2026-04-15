package com.java26groupwork.reminder.controller;

import com.java26groupwork.reminder.entity.User;
import com.java26groupwork.reminder.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        userService.register(user);
        return Map.of("msg", "ok");
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user, HttpSession session) {
        User dbUser = userService.login(user);
        session.setAttribute("userId", dbUser.getId());
        session.setAttribute("username", dbUser.getUsername());
        return Map.of("msg", "ok");
    }

    @GetMapping("/logout")
    public Map<String, String> logout(HttpSession session) {
        session.invalidate();
        return Map.of("msg", "ok");
    }
}
