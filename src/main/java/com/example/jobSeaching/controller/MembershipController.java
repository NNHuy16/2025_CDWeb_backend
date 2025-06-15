package com.example.jobSeaching.controller;

import com.example.jobSeaching.dto.response.MembershipOrderDTO;
import com.example.jobSeaching.dto.response.UserMembershipDTO;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.UserMembership;
import com.example.jobSeaching.mapper.MembershipOrderMapper;
import com.example.jobSeaching.mapper.UserMembershipMapper;
import com.example.jobSeaching.repository.MembershipOrderRepository;
import com.example.jobSeaching.repository.UserMembershipRepository;
import com.example.jobSeaching.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/membership")
@RequiredArgsConstructor
public class MembershipController {

    private final UserRepository userRepository;
    private final UserMembershipRepository userMembershipRepository;
    private final MembershipOrderRepository membershipOrderRepository;
    private final MembershipOrderMapper membershipOrderMapper;
    private final UserMembershipMapper userMembershipMapper;


    /**
     * API lấy thông tin gói thành viên (membership) hiện tại của người dùng.
     * Chỉ người dùng có vai trò USER hoặc EMPLOYER mới được truy cập.
     *
     * @param auth Thông tin xác thực hiện tại (JWT).
     * @return Trả về thông tin gói thành viên hiện tại dưới dạng DTO, hoặc noContent nếu không có.
     */
    @PreAuthorize("hasRole('EMPLOYER') or hasRole('USER')")
    @GetMapping("/current")
    public ResponseEntity<UserMembershipDTO> getCurrent(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        return userMembershipRepository.findByUser(user)
                .map(userMembershipMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }


    /**
     * API lấy danh sách các đơn hàng mua gói membership (lịch sử các giao dịch).
     * Người dùng không cần role cụ thể, miễn là đã đăng nhập.
     *
     * @param auth Thông tin xác thực hiện tại.
     * @return Trả về danh sách DTO các đơn hàng membership đã mua.
     */
    @GetMapping("/orders/my")
    public ResponseEntity<List<MembershipOrderDTO>> getMyOrders(Authentication auth) {
        String email = auth.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        List<MembershipOrderDTO> orderDTOs = membershipOrderRepository.findByUser(user)
                .stream()
                .map(membershipOrderMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(orderDTOs);
    }

}
