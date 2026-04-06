package com.Zorvyn.FinanceApp.dto.response;

import java.time.LocalDateTime;

import com.Zorvyn.FinanceApp.enums.Roles;
import com.Zorvyn.FinanceApp.enums.Status;

public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Roles role;
    private Status status;
    private LocalDateTime createdAt;

    public UserResponse(){
        
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public UserResponse(Long id, String name, String email, Roles role, Status status, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.status = status;
        this.createdAt = createdAt;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public String getEmail() {
        return email;
    }
    public Roles getRole() {
        return role;
    }
    public Status getStatus() {
        return status;
    }


}
