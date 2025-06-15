package com.example.jobSeaching.service;

import com.example.jobSeaching.entity.MembershipOrder;

import java.util.List;

public interface MembershipApprovalService {
    void approveOrder(String txnRef);
    void rejectOrder(String txnRef);
    List<MembershipOrder> getPendingOrders();

}

