package com.example.jobSeaching.service;

import com.example.jobSeaching.dto.LoginRequest;
import com.example.jobSeaching.dto.LoginResponse;
import com.example.jobSeaching.dto.RegisterRequest;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.entity.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.List;
import java.util.Optional;

public interface UsersService {
    LoginResponse login(LoginRequest request);
    User registerUserLocal(RegisterRequest registerRequest);
    User loginReisterUserGoogle(OAuth2AuthenticationToken auth);
    Optional<User> getUserById(Long id);
    Optional<User> getUserByEmail(String email);
    List<User> getAllUsers();
    List<User> getUsersByRole(Role role);
    User updateUserProfile(String email, User newUserData);
    void deleteUser(Long id);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    void changePassword(String email, String oldPassword, String newPassword);
    void requestChangeEmail(String currentEmail, String newEmail);
    void confirmChangeEmail(String currentEmail, String otp);
}
