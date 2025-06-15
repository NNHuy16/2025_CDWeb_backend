package com.example.jobSeaching.dto.response;

import com.example.jobSeaching.entity.enums.MembershipType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserMembershipDTO {
    private Long id;
    private boolean active;
    private LocalDate startDate;
    private LocalDate endDate;
    private MembershipType membershipType;
    private int allowedPosts;
    private Long userId;
}
