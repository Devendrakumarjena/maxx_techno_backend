package com.example.maxxengg.Model;

import java.util.Set;

public class LoginResponse {
    private String message;
    private String email;
    private Set<String> roles;

    public LoginResponse(String message, String email, Set<String> roles) {
        this.message = message;
        this.email = email;
        this.roles = roles;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}

