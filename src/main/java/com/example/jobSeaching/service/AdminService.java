package com.example.jobSeaching.service;

import com.example.jobSeaching.entity.Job;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.enums.JobStatus;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUsers(User user) {
        if (user.getRole() == null ) {
            user.setRole(Role.USER);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public Optional<User> getAdminById(Long id) {
        return userRepository.findById(id)
                .filter(user -> user.getRole() == Role.ADMIN);
    }

    public Optional<User> getAdminByEmail(String email) {
        return userRepository.findByEmail(email)
                .filter(user -> user.getRole() == Role.ADMIN);
    }

    public List<User> getAllAdmins() {
        return userRepository.findByRole(Role.ADMIN);
    }

    public User updateAdmin(User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setRole(Role.ADMIN); // đảm bảo không đổi vai trò khác
        return userRepository.save(user);
    }

    public void deleteAdmin(Long id) {
        userRepository.deleteById(id);
    }
}
