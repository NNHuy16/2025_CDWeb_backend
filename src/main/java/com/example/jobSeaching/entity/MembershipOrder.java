package com.example.jobSeaching.entity;

import com.example.jobSeaching.entity.enums.MembershipType;
import com.example.jobSeaching.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
public class MembershipOrder {
    @Id
    private String txnRef; // Mã giao dịch (vnp_TxnRef)

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private MembershipType membershipType;

    private int amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime createdAt;

}
