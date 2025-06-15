package com.example.jobSeaching.controller;

import com.example.jobSeaching.dto.response.MembershipOrderDTO;
import com.example.jobSeaching.dto.response.UserDTO;
import com.example.jobSeaching.entity.MembershipOrder;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.mapper.MembershipOrderMapper;
import com.example.jobSeaching.mapper.UserMapper;
import com.example.jobSeaching.repository.MembershipOrderRepository;
import com.example.jobSeaching.service.AdminService;
import com.example.jobSeaching.service.MembershipApprovalService;
import com.example.jobSeaching.service.UsersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UsersService userService;
    private final AdminService adminService;
    private final MembershipApprovalService membershipApprovalService;
    private final UserMapper userMapper;
    private final MembershipOrderRepository membershipOrderRepository;
    private final MembershipOrderMapper membershipOrderMapper;

    public AdminController(
            UsersService userService,
            AdminService adminService,
            MembershipApprovalService membershipApprovalService,
            UserMapper userMapper, MembershipOrderRepository membershipOrderRepository, MembershipOrderMapper membershipOrderMapper
    ) {
        this.userService = userService;
        this.adminService = adminService;
        this.membershipApprovalService = membershipApprovalService;
        this.userMapper = userMapper;
        this.membershipOrderRepository = membershipOrderRepository;
        this.membershipOrderMapper = membershipOrderMapper;
    }

    /**
     * Tạo mới một người dùng (chỉ Admin có quyền).
     * @param user Đối tượng người dùng cần tạo.
     * @return Trả về người dùng sau khi được lưu thành công.
     */

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(adminService.createUsers(user));
    }

    /**
     * Lấy thông tin chi tiết người dùng theo ID (chỉ Admin).
     * @param id ID của người dùng.
     * @return Trả về thông tin người dùng dưới dạng DTO.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(userMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lấy danh sách tất cả người dùng. Có thể lọc theo vai trò (role).
     * @param role (Tùy chọn) Vai trò cần lọc (USER, EMPLOYER, ADMIN).
     * @return Danh sách người dùng dưới dạng DTO.
     */
    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(@RequestParam(required = false) Role role) {
        List<User> users = userService.getAllUsers();
        if (role != null) {
            users = users.stream()
                    .filter(u -> u.getRole() == role)
                    .collect(Collectors.toList());
        }

        List<UserDTO> dtos = users.stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }


    /**
     * Xóa người dùng theo ID (chỉ Admin).
     * @param id ID của người dùng cần xóa.
     * @return Trả về mã phản hồi HTTP 204 No Content nếu thành công.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Duyệt đơn hàng mua gói thành viên (membership) dựa trên mã giao dịch (txnRef).
     * @param txnRef Mã giao dịch của đơn hàng.
     * @return Trả về thông báo kết quả duyệt đơn.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/membership-orders/approve")
    public ResponseEntity<String> approveMembership(@RequestParam String txnRef) {
        try {
            membershipApprovalService.approveOrder(txnRef);
            return ResponseEntity.ok("Đã duyệt đơn hàng thành công");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Từ chối đơn hàng mua gói thành viên dựa trên mã giao dịch (txnRef).
     * @param txnRef Mã giao dịch của đơn hàng.
     * @return Trả về thông báo kết quả từ chối đơn.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/membership-orders/reject")
    public ResponseEntity<String> rejectMembership(@RequestParam String txnRef) {
        try {
            membershipApprovalService.rejectOrder(txnRef);
            return ResponseEntity.ok("Đã từ chối đơn hàng");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Lấy danh sách các đơn hàng gói thành viên đang chờ xử lý.
     * @return Danh sách đơn hàng dưới dạng DTO.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/membership-orders/pending")
    public ResponseEntity<List<MembershipOrderDTO>> getPendingOrders() {
        List<MembershipOrder> pendingOrders = membershipApprovalService.getPendingOrders();
        List<MembershipOrderDTO> result = pendingOrders.stream()
                .map(membershipOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    /**
     * Lấy danh sách đơn hàng theo ID người dùng.
     * @param userId ID của người dùng.
     * @return Danh sách các đơn hàng của người dùng đó.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/membership-orders/by-user")
    public ResponseEntity<List<MembershipOrderDTO>> getOrdersByUserId(@RequestParam Long userId) {
        List<MembershipOrder> orders = membershipOrderRepository.findByUser_Id(userId);
        List<MembershipOrderDTO> result = orders.stream()
                .map(membershipOrderMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }


    /**
     * Lấy thông tin chi tiết đơn hàng dựa trên mã giao dịch (txnRef).
     * @param txnRef Mã giao dịch của đơn hàng.
     * @return Đơn hàng dưới dạng DTO nếu tồn tại.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/membership-orders/by-txn-ref")
    public ResponseEntity<MembershipOrderDTO> getOrderByTxnRef(@RequestParam String txnRef) {
        Optional<MembershipOrder> orderOpt = membershipOrderRepository.findByTxnRef(txnRef);
        return orderOpt
                .map(order -> ResponseEntity.ok(membershipOrderMapper.toDTO(order)))
                .orElse(ResponseEntity.notFound().build());
    }

}
