package com.example.jobSeaching.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class JobDTO {
    private Long id;
    private String title;
    private String description;
    private String salary;
    private String location;
    private String position;
    private String employerName;
    private String status;
    private LocalDate postedDate;
    private LocalDate deadline;
}
