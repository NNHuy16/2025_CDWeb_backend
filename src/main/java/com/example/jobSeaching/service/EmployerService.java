package com.example.jobSeaching.service;

import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmployerService {

    private final UserRepository userRepository;

    public EmployerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getEmployerById(Long id) {
        return userRepository.findById(id)
                .filter(user -> user.getRole() == Role.EMPLOYER);
    }

    public Optional<User> getEmployerByEmail(String email) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getRole() == Role.EMPLOYER);
    }

    public List<User> getAllEmployers() {
        return userRepository.findByRole(Role.EMPLOYER);
    }

    public User updateEmployer(User user) {
        user.setRole(Role.EMPLOYER); // đảm bảo không đổi vai trò khác
        return userRepository.save(user);
    }

    public void deleteEmployer(Long id) {
        userRepository.deleteById(id);
    }
}
