package com.example.maxxengg.Controller;

import com.example.maxxengg.Model.*;
import com.example.maxxengg.Repository.RoleRepository;
import com.example.maxxengg.Repository.UserRepository;
import com.example.maxxengg.Service.interfaces.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;  // Using UserService interface

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getUsers")
    public List<User> getUsers(){
        return userRepository.findAll();
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        try {
            // Check if the user with the email already exists
            if (userService.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Email is already in use!");
            }

            // Save the user (password will be encoded in the service layer.....)
            userService.saveUser(user);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("User registered successfully");

        } catch (DataIntegrityViolationException ex) {
            // This can happen if there's a constraint violation (e.g., unique email)
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid data or constraint violation!");

        } catch (Exception ex) {
            // Generic error handling
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing your request.");
        }
    }



    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return userService.verifyUser(loginRequest);
    }

    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRoleToUser(@RequestBody RoleAssignmentRequest request) {
        // Find the user by email
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Find the role by name
        Role role = roleRepository.findByName(request.getRoleName());
        if (role == null) {
            return ResponseEntity.badRequest().body("Role not found");
        }

        // Assign the role to the user
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userService.saveUser(user); // Save the user with the new role
        }

        return ResponseEntity.ok("Role assigned successfully");
    }
}
