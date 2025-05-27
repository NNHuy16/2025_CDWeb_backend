package com.example.jobSeaching.service;

import com.example.jobSeaching.entity.Job;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    // Sửa tham số từ Employer thành User (role=EMPLOYER)
    public List<Job> getJobsByEmployer(User employer) {
        return jobRepository.findByEmployer(employer);
    }

    public List<Job> getJobsByStatus(String status) {
        return jobRepository.findByStatus(status);
    }

    public Job updateJob(Job job) {
        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }
}
