package com.example.jobSeaching.entity;

import com.example.jobSeaching.entity.enums.MembershipType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    private LocalDate membershipStartDate;
    private LocalDate membershipEndDate;
    private int postLimit;

    @OneToOne(mappedBy = "membership", cascade = CascadeType.ALL)
    private User user;
}
