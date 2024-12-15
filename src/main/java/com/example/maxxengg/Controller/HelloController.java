package com.example.maxxengg.Controller;

import com.example.maxxengg.Model.LoginRequest;
import com.example.maxxengg.Repository.UserRepository;
import com.example.maxxengg.Service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.maxxengg.Model.User;
import java.util.List;

@Slf4j
@RestController
public class HelloController {

    @Autowired
    public UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public String hello() {
        return "hello there";
    }

    @PostMapping("/")
    public String sayHelloPost(){
        return "hello there from post";
    }

    @GetMapping("/getUsers")
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/addUser")
    public User addUser(@RequestBody User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest){
        return userService.verifyUser(loginRequest);

    }



}
