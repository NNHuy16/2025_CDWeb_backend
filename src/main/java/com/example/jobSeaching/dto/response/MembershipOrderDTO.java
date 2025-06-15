package com.example.jobSeaching.dto.response;

import com.example.jobSeaching.entity.enums.MembershipType;
import com.example.jobSeaching.entity.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MembershipOrderDTO {
    private String txnRef;
    private UserDTO user; // Hoặc userId nếu bạn không muốn nested
    private MembershipType membershipType;
    private int amount;
    private PaymentStatus status;
    private String paymentMethod;
    private boolean adminApproved;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
}
