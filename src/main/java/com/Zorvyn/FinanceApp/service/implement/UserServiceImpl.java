package com.Zorvyn.FinanceApp.service.implement;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Zorvyn.FinanceApp.dto.request.UserRequest;
import com.Zorvyn.FinanceApp.dto.response.ApiResponse;
import com.Zorvyn.FinanceApp.dto.response.PagedResponse;
import com.Zorvyn.FinanceApp.dto.response.UserResponse;
import com.Zorvyn.FinanceApp.entity.User;
import com.Zorvyn.FinanceApp.enums.Roles;
import com.Zorvyn.FinanceApp.enums.Status;
import com.Zorvyn.FinanceApp.exception.BadRequestException;
import com.Zorvyn.FinanceApp.exception.ResourceNotFoundException;
import com.Zorvyn.FinanceApp.repository.UserRepository;
import com.Zorvyn.FinanceApp.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserResponse createUser(UserRequest request) {
          try {
            logger.info("Admin creating user with email: {}", request.getEmail());

            // check required fields
            if (request.getName() == null || request.getName().trim().isEmpty()) {
                throw new BadRequestException("Name cannot be blank");
            }
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                throw new BadRequestException("Email cannot be blank");
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                throw new BadRequestException("Password cannot be blank");
            }
            if (request.getPassword().length() < 6) {
                throw new BadRequestException("Password must be at least 6 characters");
            }
            if (request.getRole() == null) {
                throw new BadRequestException("Role cannot be blank");
            }
             if (request.getStatus() == null) {
                throw new BadRequestException("Status cannot be blank");
            }

            // Check if a user with this email already exists
            String email = request.getEmail().trim().toLowerCase();
            userRepository.findByEmail(email).ifPresent(existingUser -> {
                throw new BadRequestException("User with email '" + email + "' already exists");
            });

            User user = new User();
            user.setName(request.getName().trim());
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            user.setStatus(request.getStatus());

            User savedUser = userRepository.save(user);

            logger.info("User created successfully with email: {}", request.getEmail());
            return mapToResponse(savedUser);

        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error creating user: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public UserResponse getUserById(Long id) {
        try {
            logger.info("Fetching user with id: {}", id);

            if (id == null) {
                throw new BadRequestException("User id cannot be blank");
            }

            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("User not found with id: {}", id);
                        return new ResourceNotFoundException("User not found with id: " + id);
                    });

            logger.info("User fetched successfully with id: {}", id);
            return mapToResponse(user);

        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error fetching user by id: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public List<UserResponse> getAllUsers(String search, Roles roles, Status status) {
        try {
            logger.info("Fetching all users - search: {}, role: {}, status: {}", search, roles, status);

            List<User> users = userRepository.findAllWithFilters(search, roles, status);

            if (users.isEmpty()) {
                logger.info("No users found");
                return List.of();
            }

            logger.info("Found {} users", users.size());

            return users.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Unexpected error fetching all users: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public PagedResponse<UserResponse> getAllUsers(String search, Roles roles, Status status, int page, int size) {
        try {
            logger.info("Fetching users (paginated) - search: {}, role: {}, status: {}, page: {}, size: {}",
                    search, roles, status, page, size);

            Page<User> userPage = userRepository.findAllWithFilters(search, roles, status, PageRequest.of(page, size));

            List<UserResponse> content = userPage.getContent().stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            logger.info("Found {} users on page {} of {}", content.size(), page, userPage.getTotalPages());

            return new PagedResponse<>(
                    content,
                    userPage.getNumber(),
                    userPage.getSize(),
                    userPage.getTotalElements(),
                    userPage.getTotalPages(),
                    userPage.isLast()
            );

        } catch (Exception e) {
            logger.error("Unexpected error fetching paginated users: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public List<UserResponse> getPendingUsers() {
        try{
            logger.info("fetching Inactive users");
            List<User> pendingUsers = userRepository.findByStatus(Status.INACTIVE);
            logger.info("Found {} pending users", pendingUsers.size());
            return pendingUsers.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        }catch(Exception e){
            logger.error("Unexpected error fetching pending users: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest updatedUser) {
        try{

            logger.info("updating user with id : {}" , id);
            if(id == null){
                throw new BadRequestException("User id cannot be blank");
            }
            User user = userRepository.findById(id)
            .orElseThrow(()-> {
                logger.warn("User not found with id: {}", id);
                return new ResourceNotFoundException("User not found by id : " +id);
            });

            if (updatedUser.getName() != null && !updatedUser.getName().trim().isEmpty()) {
                user.setName(updatedUser.getName().trim());
            }

            if (updatedUser.getEmail() != null && !updatedUser.getEmail().trim().isEmpty()) {

                String email = updatedUser.getEmail().trim().toLowerCase();

                if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    throw new BadRequestException("Invalid email format");
                    }

                    // check duplicate (if changed)
                    userRepository.findByEmail(email).ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(id)) {
                            throw new BadRequestException("Email already in use");
                        }
                    });

                user.setEmail(email);
            }
 
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().trim().isEmpty()) {
                if (updatedUser.getPassword().length() < 6) {
                    throw new BadRequestException("Password must be at least 6 characters");
                }
                user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            }
             if (updatedUser.getStatus() != null) {
            user.setStatus(updatedUser.getStatus());
            }

            if (updatedUser.getRole() != null) {
                user.setRole(updatedUser.getRole());
            }
            User savedUser = userRepository.save(user);

            logger.info("User updated successfully with id: {}", id);

            return mapToResponse(savedUser);
            
        }catch(BadRequestException | ResourceNotFoundException e){
            throw e;
        }catch(Exception e){
            logger.error("Unexpected Error During user :{}" , e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again");
        }
    }

    @Override
    public ApiResponse deleteUser(Long id) {
          try {
            logger.info("Deleting user with id: {}", id);

            if (id == null) {
                throw new BadRequestException("User id cannot be blank");
            }

            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("User not found with id: {}", id);
                        return new ResourceNotFoundException("User not found with id: " + id);
                    });

            // Prevent admin from deleting themselves
            String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            if (user.getEmail().equalsIgnoreCase(currentEmail)) {
                throw new BadRequestException("Admin cannot delete their own account");
            }

            userRepository.delete(user);

            logger.info("User deleted successfully with id: {}", id);
            return new ApiResponse(true, "User deleted successfully");

        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error deleting user: {}", e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }


    public UserResponse mapToResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getRole(),
            user.getStatus(),
            user.getCreatedAt()
        );
    }



}
