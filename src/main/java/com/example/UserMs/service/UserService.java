package com.example.UserMs.service;

import com.example.UserMs.entity.User;
import com.example.UserMs.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(User user) {
        // Check if user EXISTS - if yes, throw error
        if (userRepo.findByUserName(user.getUserName()).isPresent()) {
            throw new RuntimeException("User Already Exists!");
        }

        // Create NEW user with encoded password
        User newUser = new User();
        newUser.setUserName(user.getUserName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setRole(user.getRole());

        return userRepo.save(newUser);
    }



}
