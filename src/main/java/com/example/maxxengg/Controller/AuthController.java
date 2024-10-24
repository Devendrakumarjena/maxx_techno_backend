package com.example.maxxengg.Controller;

import com.example.maxxengg.Model.User;
import com.example.maxxengg.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService userService;  // Using UserService interface

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody User user) {
        try {
            // Check if the user with the email already exists
            if (userService.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Email is already in use!");
            }

            // Save the user (password will be encoded in the service layer)
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
}
