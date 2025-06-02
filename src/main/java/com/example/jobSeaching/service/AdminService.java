package com.example.jobSeaching.service;

import com.example.jobSeaching.entity.User;

import java.util.List;
import java.util.Optional;

public interface AdminService {
    User createUsers(User user);

    Optional<User> getAdminById(Long id);

    Optional<User> getAdminByEmail(String email);

    List<User> getAllAdmins();

    User updateAdmin(User user);

    void deleteAdmin(Long id);

}
