package com.Zorvyn.FinanceApp.dto.request;

import com.Zorvyn.FinanceApp.enums.Roles;
import com.Zorvyn.FinanceApp.enums.Status;

public class UserRequest {

    private String name;
    private String email;
    private String password;
    private Roles role;
    private Status status;

    public UserRequest(){
        
    }
    
    public UserRequest(String name, String email, String password, Roles role, Status status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public Roles getRole() {
        return role;
    }
    public void setRole(Roles role) {
        this.role = role;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}
