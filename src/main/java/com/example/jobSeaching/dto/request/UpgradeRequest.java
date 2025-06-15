package com.example.jobSeaching.dto.request;


import com.example.jobSeaching.entity.enums.MembershipType;
import lombok.Data;

@Data
public class UpgradeRequest {
    private MembershipType membershipType;

    // Getter, Setter
}
