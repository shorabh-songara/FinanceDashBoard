package com.Zorvyn.FinanceApp.service;

import java.util.List;

import com.Zorvyn.FinanceApp.dto.request.UserRequest;
import com.Zorvyn.FinanceApp.dto.response.ApiResponse;
import com.Zorvyn.FinanceApp.dto.response.PagedResponse;
import com.Zorvyn.FinanceApp.dto.response.UserResponse;
import com.Zorvyn.FinanceApp.entity.User;
import com.Zorvyn.FinanceApp.enums.Roles;
import com.Zorvyn.FinanceApp.enums.Status;

public interface UserService {

    UserResponse createUser(UserRequest user);

    UserResponse getUserById(Long id);

    List<UserResponse> getAllUsers(String search, Roles roles, Status status);

    PagedResponse<UserResponse> getAllUsers(String search, Roles roles, Status status, int page, int size);

    List<UserResponse> getPendingUsers();

    UserResponse updateUser(Long id, UserRequest updatedUser);

    ApiResponse deleteUser(Long id);

    UserResponse mapToResponse(User user);

}

