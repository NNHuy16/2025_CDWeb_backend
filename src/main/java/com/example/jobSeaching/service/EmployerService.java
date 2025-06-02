package com.example.jobSeaching.service;

import com.example.jobSeaching.entity.User;

import java.util.List;
import java.util.Optional;

public interface EmployerService {
    Optional<User> getEmployerById(Long id);
    Optional<User> getEmployerByEmail(String email);
    List<User> getAllEmployers();
    User updateEmployer(User user);
    void deleteEmployer(Long id);
}
