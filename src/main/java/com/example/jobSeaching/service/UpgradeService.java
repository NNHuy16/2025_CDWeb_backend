package com.example.jobSeaching.service;

import com.example.jobSeaching.dto.UpgradeRequest;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.enums.MembershipType;

public interface UpgradeService {
    void upgradeMembership(UpgradeRequest request);
    void notifyAdminUserBoughtMembership(User user, MembershipType membershipType);
}
