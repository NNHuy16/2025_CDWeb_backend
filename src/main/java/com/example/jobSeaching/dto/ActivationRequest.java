package com.example.jobSeaching.dto;

import com.example.jobSeaching.entity.enums.MembershipType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivationRequest {

    private String key;
    private boolean activated;
    private MembershipType membershipType;
    // getters & setters
}
