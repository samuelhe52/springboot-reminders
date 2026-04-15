package com.java26groupwork.reminder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.java26groupwork.reminder.mapper")
public class ReminderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReminderApplication.class, args);
    }
}
