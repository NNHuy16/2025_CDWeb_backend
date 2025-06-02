package com.example.jobSeaching.dto;


import com.example.jobSeaching.entity.enums.MembershipType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpgradeRequest {
    private MembershipType membershipType;

    // Getter, Setter
}
