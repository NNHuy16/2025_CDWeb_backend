package com.example.jobSeaching.controller;

import com.example.jobSeaching.dto.request.JobRequest;
import com.example.jobSeaching.dto.response.JobDTO;
import com.example.jobSeaching.entity.enums.JobStatus;
import com.example.jobSeaching.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping("/post-job")
    public ResponseEntity<JobDTO> postJob(@RequestBody JobRequest jobRequest, Authentication authentication) {
        return ResponseEntity.ok(jobService.createJob(jobRequest, authentication));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<JobDTO> updateJobStatus(
            @PathVariable Long id,
            @RequestParam JobStatus status,
            Authentication authentication) {
        String adminEmail = authentication.getName();
        return ResponseEntity.ok(jobService.updateJobStatus(id, status, adminEmail));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDTO> getJobById(@PathVariable Long id) {
        JobDTO job = jobService.getJobById(id);
        return job != null ? ResponseEntity.ok(job) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<JobDTO>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<JobDTO> updateJob(@PathVariable Long id, @RequestBody JobRequest jobRequest) {
        return ResponseEntity.ok(jobService.updateJob(jobRequest, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}
