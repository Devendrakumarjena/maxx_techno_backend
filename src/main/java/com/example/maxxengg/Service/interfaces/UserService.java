package com.example.maxxengg.Service.interfaces;

import com.example.maxxengg.Model.LoginRequest;
import com.example.maxxengg.Model.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService
{
    User saveUser(User user);

    Optional<User> findByEmail(String email);

    ResponseEntity<String> verifyUser(LoginRequest loginRequest);

}
