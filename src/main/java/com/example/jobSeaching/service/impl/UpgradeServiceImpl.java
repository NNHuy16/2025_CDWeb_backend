package com.example.jobSeaching.service.impl;

import com.example.jobSeaching.dto.request.UpgradeRequest;
import com.example.jobSeaching.entity.AdminNotification;
import com.example.jobSeaching.entity.MembershipOrder;
import com.example.jobSeaching.entity.MembershipPlan;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.enums.MembershipType;
import com.example.jobSeaching.entity.enums.PaymentStatus;
import com.example.jobSeaching.repository.AdminNotificationRepository;
import com.example.jobSeaching.repository.MembershipOrderRepository;
import com.example.jobSeaching.repository.MembershipPlanRepository;
import com.example.jobSeaching.repository.UserRepository;
import com.example.jobSeaching.service.NotificationSender;
import com.example.jobSeaching.service.UpgradeService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class UpgradeServiceImpl implements UpgradeService {

    private final UserRepository userRepository;
    private final AdminNotificationRepository notificationRepository;
    private final NotificationSender notificationSender;
    private final MembershipOrderRepository membershipOrderRepository;
    private final MembershipPlanRepository membershipPlanRepository;

    @Override
    public void upgradeMembership(UpgradeRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        MembershipType membershipType = request.getMembershipType();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Lấy thông tin gói từ DB
        MembershipPlan plan = membershipPlanRepository.findById(membershipType)
                .orElseThrow(() -> new RuntimeException("Membership plan không tồn tại"));

        // Tạo đơn hàng mới
        MembershipOrder order = new MembershipOrder();
        order.setTxnRef("LOCAL_" + System.currentTimeMillis()); // hoặc UUID.randomUUID().toString()
        order.setUser(user);
        order.setMembershipPlan(plan);
        order.setStatus(PaymentStatus.PENDING);
        order.setPaymentMethod("LOCAL");
        order.setAmount(plan.getPrice());
        order.setAdminApproved(false);
        order.setCreatedAt(LocalDateTime.now());

        membershipOrderRepository.save(order);

    // Gửi thông báo cho Admin
        notifyAdminUserBoughtMembership(user, request.getMembershipType());
    }

    @Override
    public void notifyAdminUserBoughtMembership(User user, MembershipType membershipType) {
        String message = "Người dùng " + user.getFullName() + " đã thanh toán gói " + membershipType.name() + " – chờ admin kích hoạt.";
        AdminNotification notification = new AdminNotification();
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        notificationSender.sendToAdmin(message); // Gửi realtime nếu có WebSocket
    }
}
