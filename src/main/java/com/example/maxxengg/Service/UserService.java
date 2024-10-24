package com.example.maxxengg.Service;

import com.example.maxxengg.Model.User;

import java.util.Optional;

public interface UserService {
    User saveUser(User user);
    Optional<User> findByEmail(String email);
}
