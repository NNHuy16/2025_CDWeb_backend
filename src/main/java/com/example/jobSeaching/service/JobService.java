package com.example.jobSeaching.service;

import aj.org.objectweb.asm.commons.Remapper;
import com.example.jobSeaching.dto.JobRequest;
import com.example.jobSeaching.entity.Job;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.enums.JobStatus;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.helper.SecurityUtil;
import com.example.jobSeaching.repository.JobRepository;
import com.example.jobSeaching.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

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
        job.setStatus(JobStatus.PENDING);
        job.setEmployer(user);

        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
    }

    public List<Job> getAllJobs() {
        return null;
    }

    public Job updateJob(Job job) {
        return null;
    }

    public Remapper getJobById(Long id) {
        return null;
    }

    public Job updateJobStatus(Long jobId, String statusStr) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        JobStatus status = JobStatus.valueOf(statusStr.toUpperCase()); // sẽ throw nếu sai
        if (status != JobStatus.APPROVED && status != JobStatus.REJECTED) {
            throw new IllegalArgumentException("Only APPROVED or REJECTED are allowed");
        }

        job.setStatus(status);
        return jobRepository.save(job);
    }

}
