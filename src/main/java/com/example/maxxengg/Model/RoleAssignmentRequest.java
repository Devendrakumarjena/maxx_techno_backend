package com.example.maxxengg.Model;

public class RoleAssignmentRequest {
    private String email;
    private String roleName;

    // Constructors
    public RoleAssignmentRequest() {}

    public RoleAssignmentRequest(String email, String roleName) {
        this.email = email;
        this.roleName = roleName;
    }

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

