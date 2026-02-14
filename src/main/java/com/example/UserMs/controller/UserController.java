package com.example.UserMs.controller;

import com.example.UserMs.entity.User;
import com.example.UserMs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ums")
public class UserController {
    @Autowired
    UserService userService;

    // 1. Public API to Create User
    @PostMapping("/createUser")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    // 2. Protected API (Requires Login)
    @GetMapping("/hello")
    public String hello() {
        return "Hello World! You are authenticated.";
    }

}
