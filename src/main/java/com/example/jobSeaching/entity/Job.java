package com.example.jobSeaching.entity;

import com.example.jobSeaching.entity.enums.JobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String title;
    private String description;
    private String requirements;
    private String salary;
    private String location;
    private String contact;
    private String companyName;
    private String position;
    private String age;
    private String education;
    private int quantity;
    private String workTime;
    private String recruitment;
    private String experience;

    private LocalDate postedDate;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private JobStatus status; // PENDING, APPROVED, CLOSED


    @ManyToOne
    @JoinColumn(name = "employer_id")
    private User employer;

    @OneToMany(mappedBy = "job")
    private List<Application> applications;

}