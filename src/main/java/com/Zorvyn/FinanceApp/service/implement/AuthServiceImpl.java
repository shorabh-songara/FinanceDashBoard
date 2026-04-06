package com.Zorvyn.FinanceApp.service.implement;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.Zorvyn.FinanceApp.dto.request.LoginRequest;
import com.Zorvyn.FinanceApp.dto.request.RegisterRequest;
import com.Zorvyn.FinanceApp.dto.response.ApiResponse;
import com.Zorvyn.FinanceApp.dto.response.LoginResponse;
import com.Zorvyn.FinanceApp.entity.User;
import com.Zorvyn.FinanceApp.enums.Roles;
import com.Zorvyn.FinanceApp.enums.Status;
import com.Zorvyn.FinanceApp.exception.BadRequestException;
import com.Zorvyn.FinanceApp.exception.ResourceNotFoundException;
import com.Zorvyn.FinanceApp.repository.UserRepository;
import com.Zorvyn.FinanceApp.service.AuthService;
import com.Zorvyn.FinanceApp.service.JwtService;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Override
    public LoginResponse login(LoginRequest request) {
        logger.info("Login attempt for email: {}", request.getEmail());

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                logger.warn("Login failed - email is blank");
                throw new BadRequestException("Email cannot be blank");
            }

        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            logger.warn("Login failed - password is blank");
            throw new BadRequestException("Password cannot be blank");
        }
        try{
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> {
                        logger.warn("Login failed - email not found: {}", request.getEmail());
                        return new ResourceNotFoundException("Invalid email");
                    });
            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                logger.warn("Login failed - wrong password for email: {}", request.getEmail());
                throw new ResourceNotFoundException("Invalid password");
            }
            if (user.getStatus() == Status.INACTIVE) {
                logger.warn("Login failed - inactive account for email: {}", request.getEmail());
                throw new ResourceNotFoundException(
                    "Your account is inactive. Please contact admin for activation."
                );
            }

            String token = jwtService.generateToken(user.getEmail(), user.getRole(), user.getId());

            logger.info("Login successful for email: {}", request.getEmail());

            return new LoginResponse(
                token,
                "Login successful. Welcome back " + user.getName() + "."
            );

        } catch (BadRequestException | ResourceNotFoundException e) {
            throw e;
        }catch (Exception e) {
            logger.error("Unexpected error during login for email: {} - {}",
                request.getEmail(), e.getMessage());
            throw new RuntimeException("Something went wrong. Please try again.");
        }
    }

    @Override
    public ApiResponse register(RegisterRequest register) {

        if (register.getName() == null || register.getName().trim().isEmpty()) {
        return new ApiResponse(false, "Name is required");
        }

        if (register.getEmail() == null || register.getEmail().trim().isEmpty()) {
            return new ApiResponse(false, "Email is required");
        }
        if (!register.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            return new ApiResponse(false, "Invalid email format");
        }

        if (register.getPassword() == null || register.getPassword().trim().isEmpty()) {
            return new ApiResponse(false, "Password is required");
        }
        
        try{
            String email = register.getEmail().trim().toLowerCase();

            if (userRepository.findByEmail(email).isPresent()) {
                return new ApiResponse(false, "Email already registered");
            }

            User newUser = new User();
            newUser.setName(register.getName());
            newUser.setEmail(register.getEmail());
            newUser.setPassword(passwordEncoder.encode(register.getPassword()));

            newUser.setRole(Roles.VIEWER);
            newUser.setStatus(Status.INACTIVE);     

            userRepository.save(newUser);
            return new ApiResponse(true, "User registered successfully");

        }catch(Exception ex){
            logger.error("Error during registration: {}" , ex.getMessage());
            return new ApiResponse(false , "Something went Wrong");
        }

    }

    // @Override
    // public ApiResponse logout(String token) {
    //     try {
    //         logger.info("Logout attempt");

    //         // check token is not blank
    //         if (token == null || token.trim().isEmpty()) {
    //             throw new BadRequestException("Token cannot be blank");
    //         }

    //         logger.info("Logout successful");

    //         return new ApiResponse(true , "Logged out successfully.");

    //     } catch (BadRequestException e) {
    //         throw e;

    //     } catch (Exception e) {
    //         logger.error("Unexpected error during logout - {}", e.getMessage());
    //         throw new RuntimeException("Something went wrong. Please try again.");
    //     }
    // }

}
