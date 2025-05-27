package com.example.jobSeaching.repository;

import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);

}
