package com.example.jobSeaching.service;

import com.example.jobSeaching.entity.Application;
import com.example.jobSeaching.entity.Job;
import com.example.jobSeaching.entity.User;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    Application createApplication(Application application);
    Optional<Application> getApplicationById(Long id);
    List<Application> getAllApplications();
    List<Application> getApplicationsByUser(User user);
    List<Application> getApplicationsByJob(Job job);
    Application updateApplication(Application application);
    void deleteApplication(Long id);
}
