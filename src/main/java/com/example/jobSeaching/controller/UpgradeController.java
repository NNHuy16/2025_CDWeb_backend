package com.example.jobSeaching.controller;

import com.example.jobSeaching.dto.request.UpgradeRequest;
import com.example.jobSeaching.service.UpgradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/payment")
public class UpgradeController {

    @Autowired
    private UpgradeService upgradeService;


    /**
     * API nâng cấp tài khoản người dùng thành EMPLOYER bằng cách khởi tạo đơn hàng mua gói membership.
     * Chỉ người dùng có role 'USER' mới có thể thực hiện.
     *
     * Cơ chế:
     * - Người dùng gửi thông tin gói muốn nâng cấp (UpgradeRequest chứa planId, method, etc).
     * - Hệ thống tạo đơn hàng MembershipOrder (chờ admin duyệt).
     * - Sau khi admin phê duyệt, người dùng sẽ được nâng quyền đăng bài.
     *
     * @param request Dữ liệu yêu cầu nâng cấp membership.
     * @return Thông báo nâng cấp thành công (về mặt tạo order).
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/upgrade")
    public ResponseEntity<String> upgrade(@RequestBody UpgradeRequest request) {
        upgradeService.upgradeMembership(request);
        return ResponseEntity.ok("Nâng cấp thành công");
    }
}
