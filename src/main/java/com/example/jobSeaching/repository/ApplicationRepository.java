package com.example.jobSeaching.repository;

import com.example.jobSeaching.entity.Application;
import com.example.jobSeaching.entity.Job;
import com.example.jobSeaching.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByUser(User user);
    List<Application> findByJob(Job job);
}
