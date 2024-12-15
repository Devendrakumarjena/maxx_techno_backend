package com.example.maxxengg.Service.impl;

import com.example.maxxengg.Model.LoginRequest;
import com.example.maxxengg.Model.User;
import com.example.maxxengg.Repository.UserRepository;
import com.example.maxxengg.Jwt.JWTService;
import com.example.maxxengg.Service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public ResponseEntity<String> verifyUser(LoginRequest loginRequest) {
        Authentication auth= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        if(auth.isAuthenticated()){
            return ResponseEntity.status(HttpStatus.OK).body(jwtService.generateToken(loginRequest.getEmail()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed, Username or Password is incorrect");

    }

}
