package com.example.jobSeaching.service.impl;

import com.example.jobSeaching.entity.MembershipOrder;
import com.example.jobSeaching.entity.MembershipPlan;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.UserMembership;
import com.example.jobSeaching.entity.enums.MembershipType;
import com.example.jobSeaching.entity.enums.PaymentStatus;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.repository.MembershipOrderRepository;
import com.example.jobSeaching.repository.MembershipPlanRepository;
import com.example.jobSeaching.repository.UserMembershipRepository;
import com.example.jobSeaching.repository.UserRepository;
import com.example.jobSeaching.service.MembershipApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MembershipApprovalServiceImpl implements MembershipApprovalService {

    private final MembershipOrderRepository membershipOrderRepository;
    private final UserRepository userRepository;
    private final UserMembershipRepository userMembershipRepository;
    private final MembershipPlanRepository membershipPlanRepository;


    @Override
    public void approveOrder(String txnRef) {
        MembershipOrder order = membershipOrderRepository.findByTxnRef(txnRef)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        if (order.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Đơn hàng không ở trạng thái chờ duyệt");
        }

        User user = order.getUser();
        MembershipPlan plan = order.getMembershipPlan();

        LocalDate now = LocalDate.now();
        LocalDate end = (plan.getDurationInDays() > 0) ? now.plusDays(plan.getDurationInDays()) : null;

        // Gọi hàm đã cập nhật
        activateMembership(user, plan, now, end);

        order.setAdminApproved(true);
        order.setStatus(PaymentStatus.PAID);
        order.setApprovedAt(LocalDateTime.now());
        membershipOrderRepository.save(order);
    }


    @Override
    public void rejectOrder(String txnRef) {
        MembershipOrder order = membershipOrderRepository.findByTxnRef(txnRef)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        if (order.getStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Đơn hàng không ở trạng thái chờ duyệt");
        }

        order.setStatus(PaymentStatus.FAILED);
        order.setAdminApproved(false);
        order.setApprovedAt(LocalDateTime.now());
        membershipOrderRepository.save(order);
    }

    public void activateMembership(User user, MembershipPlan plan, LocalDate start, LocalDate end) {
        Optional<UserMembership> existing = userMembershipRepository.findByUser(user);
        if (existing.isPresent()) {
            UserMembership m = existing.get();
            m.setMembershipPlan(plan);
            m.setStartDate(start);
            m.setEndDate(end);
            m.setActive(true);
            userMembershipRepository.save(m);
        } else {
            userMembershipRepository.save(new UserMembership(null, user, plan, start, end, true));
        }

        if (user.getRole() != Role.EMPLOYER) {
            user.setRole(Role.EMPLOYER);
            userRepository.save(user);
        }
    }

    public List<MembershipOrder> getPendingOrders() {
        return membershipOrderRepository.findByStatusAndAdminApprovedFalse(PaymentStatus.PENDING);
    }


}
