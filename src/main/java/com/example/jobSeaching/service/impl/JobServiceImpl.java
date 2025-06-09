package com.example.jobSeaching.service.impl;

import com.example.jobSeaching.dto.JobRequest;
import com.example.jobSeaching.entity.Job;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.helper.SecurityUtil;
import com.example.jobSeaching.repository.JobRepository;
import com.example.jobSeaching.repository.UserRepository;
import com.example.jobSeaching.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    @Override
    public Job createJob(JobRequest jobRequest, Authentication authentication) {
        String email = SecurityUtil.getEmailFromAuth(authentication);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.EMPLOYER) {
            throw new AccessDeniedException("Only employers can post jobs.");
        }

        Job job = new Job();
        job.setTitle(jobRequest.getTitle());
        job.setDescription(jobRequest.getDescription());
        job.setRequirements(jobRequest.getRequirements());
        job.setSalary(jobRequest.getSalary());
        job.setLocation(jobRequest.getLocation());
        job.setDeadline(jobRequest.getDeadline());
        job.setPostedDate(LocalDate.now());
        job.setEmployer(user);

        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new RuntimeException("Job not found");
        }
        jobRepository.deleteById(id);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job updateJob(Job job) {
        if (!jobRepository.existsById(job.getId())) {
            throw new RuntimeException("Job not found");
        }
        return jobRepository.save(job);
    }


    @Override
    public Optional<Job> getJobById(Long id) {
        return jobRepository.findById(id);
    }


}
