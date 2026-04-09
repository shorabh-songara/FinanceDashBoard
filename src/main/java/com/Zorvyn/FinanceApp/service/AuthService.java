package com.Zorvyn.FinanceApp.service;

import com.Zorvyn.FinanceApp.dto.request.LoginRequest;
import com.Zorvyn.FinanceApp.dto.request.RegisterRequest;
import com.Zorvyn.FinanceApp.dto.response.LoginResponse;
import com.Zorvyn.FinanceApp.dto.response.ApiResponse;

public interface AuthService {

    LoginResponse login(LoginRequest login);

    ApiResponse register(RegisterRequest register);
}