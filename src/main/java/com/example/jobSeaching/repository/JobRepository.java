package com.example.jobSeaching.repository;

import com.example.jobSeaching.entity.Job;
import com.example.jobSeaching.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByEmployer(User employer);
    List<Job> findByStatus(String status);
}
