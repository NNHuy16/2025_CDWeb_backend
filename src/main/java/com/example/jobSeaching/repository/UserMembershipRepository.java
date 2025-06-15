package com.example.jobSeaching.repository;

import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.UserMembership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserMembershipRepository extends JpaRepository<UserMembership, Long> {
    Optional<UserMembership> findByUser(User user);
    boolean existsByUserAndActiveTrue(User user);
}