package com.example.jobSeaching.controller;

import com.example.jobSeaching.dto.JobRequest;
import com.example.jobSeaching.entity.Job;
import com.example.jobSeaching.entity.enums.JobStatus;
import com.example.jobSeaching.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor

@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private final JobService jobService;


    @PreAuthorize("hasRole('EMPLOYER')")
    @PostMapping
    public ResponseEntity<Job> postJob(@RequestBody JobRequest jobRequest, Authentication authentication) {
        Job job = jobService.createJob(jobRequest, authentication);
        return ResponseEntity.ok(job);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Job> updateJobStatus(
            @PathVariable Long id,
            @RequestParam JobStatus status,
            Authentication authentication) {

        String adminEmail = authentication.getName();
        return ResponseEntity.ok(jobService.updateJobStatus(id, status, adminEmail));
    }


//    @GetMapping("/{id}")
//    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
//        return jobService.getJobById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @GetMapping
//    public ResponseEntity<List<Job>> getAllJobs() {
//        return ResponseEntity.ok(jobService.getAllJobs());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Job> updateJob(@PathVariable Long id, @RequestBody Job job) {
//        job.setId(id);
//        return ResponseEntity.ok(jobService.updateJob(job));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
//        jobService.deleteJob(id);
//        return ResponseEntity.noContent().build();
//    }
}
