package com.java26groupwork.reminder.service;

import com.java26groupwork.reminder.entity.User;

public interface UserService {
    void register(User user);

    User login(User user);
}
