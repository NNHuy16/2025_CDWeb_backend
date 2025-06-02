package com.example.jobSeaching.service.impl;

import com.example.jobSeaching.dto.UpgradeRequest;
import com.example.jobSeaching.entity.AdminNotification;
import com.example.jobSeaching.entity.Membership;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.enums.MembershipType;
import com.example.jobSeaching.repository.AdminNotificationRepository;
import com.example.jobSeaching.repository.MembershipRepository;
import com.example.jobSeaching.repository.UserRepository;
import com.example.jobSeaching.service.UpgradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class UpgradeServiceImpl implements UpgradeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private AdminNotificationRepository notificationRepository;

    @Override
    public void upgradeMembership(UpgradeRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Chỉ gửi thông báo, không gán membership lúc này
        notifyAdminUserBoughtMembership(user, request.getMembershipType());
    }

    @Override
    public void notifyAdminUserBoughtMembership(User user, MembershipType membershipType) {
        String message = "Người dùng " + user.getFullName() + " đã mua gói " + membershipType.name()
                + " - chờ kích hoạt.";
        AdminNotification notification = new AdminNotification();
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());

        notificationRepository.save(notification);
    }
}

