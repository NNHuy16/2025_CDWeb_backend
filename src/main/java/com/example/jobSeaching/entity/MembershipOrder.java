package com.example.jobSeaching.entity;

import com.example.jobSeaching.entity.enums.MembershipType;
import com.example.jobSeaching.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class MembershipOrder {
    @Id
    private String txnRef;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "membership_type", referencedColumnName = "type")
    private MembershipPlan membershipPlan;

    private int amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status; // PENDING, PAID, FAILED

    private String paymentMethod; // VNPAY, LOCAL

    private boolean adminApproved; // Chỉ dùng cho payment thủ công
    private LocalDateTime approvedAt;

    private LocalDateTime createdAt;
}