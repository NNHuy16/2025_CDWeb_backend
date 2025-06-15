package com.example.jobSeaching.entity;


import com.example.jobSeaching.entity.enums.MembershipType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class MembershipPlan {
    @Id
    @Enumerated(EnumType.STRING)
    private MembershipType type;

    private String name;
    private int postLimit;
    private int durationInDays;
    private int price;
}

