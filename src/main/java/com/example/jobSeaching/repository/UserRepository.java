package com.example.jobSeaching.repository;

import com.example.jobSeaching.entity.Role;
import com.example.jobSeaching.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // Lấy tất cả người dùng theo vai trò (ADMIN, EMPLOYER, USER)
    List<User> findByRole(Role role);
}
