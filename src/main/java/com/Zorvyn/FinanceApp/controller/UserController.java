package com.Zorvyn.FinanceApp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Zorvyn.FinanceApp.dto.request.UserRequest;
import com.Zorvyn.FinanceApp.dto.response.ApiResponse;
import com.Zorvyn.FinanceApp.dto.response.UserResponse;
import com.Zorvyn.FinanceApp.enums.Roles;
import com.Zorvyn.FinanceApp.enums.Status;
import com.Zorvyn.FinanceApp.service.UserService;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(
            @RequestBody UserRequest request) {

        logger.info("Create user request received");
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Roles role,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {

        logger.info("Get all users request - search: {}, role: {}, status: {}, page: {}, size: {}",
                search, role, status, page, size);

        // if page or size is provided, return paginated response
        if (page != null || size != null) {
            int pageNum = (page != null && page >= 0) ? page : 0;
            int pageSize = (size != null && size > 0) ? size : 10;
            return ResponseEntity.ok(userService.getAllUsers(search, role, status, pageNum, pageSize));
        }

        // otherwise, return full list (backward compatible)
        List<UserResponse> response = userService.getAllUsers(search, role, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> getPendingUsers() {

        logger.info("Get pending users request received");

        List<UserResponse> response = userService.getPendingUsers();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {

        logger.info("Get user by id request for id: {}", id);
        UserResponse response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserRequest request) {

        logger.info("Update user request for id: {}", id);

        UserResponse response = userService.updateUser(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteUser(
            @PathVariable Long id) {

        logger.info("Delete user request for id: {}", id);

        ApiResponse response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }

}
