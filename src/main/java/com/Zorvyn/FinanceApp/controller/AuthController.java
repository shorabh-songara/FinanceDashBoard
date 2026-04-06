package com.Zorvyn.FinanceApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Zorvyn.FinanceApp.dto.request.LoginRequest;
import com.Zorvyn.FinanceApp.dto.request.RegisterRequest;
import com.Zorvyn.FinanceApp.dto.response.ApiResponse;
import com.Zorvyn.FinanceApp.dto.response.LoginResponse;
import com.Zorvyn.FinanceApp.service.AuthService;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

    
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(authService.login(loginRequest));
    }

}
