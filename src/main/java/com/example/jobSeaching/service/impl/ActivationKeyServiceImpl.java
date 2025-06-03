package com.example.jobSeaching.service.impl;

import com.example.jobSeaching.dto.ActivationRequest;
import com.example.jobSeaching.entity.ActivationKey;
import com.example.jobSeaching.entity.Membership;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.enums.MembershipType;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.repository.ActivationKeyRepository;
import com.example.jobSeaching.repository.MembershipRepository;
import com.example.jobSeaching.repository.UserRepository;
import com.example.jobSeaching.service.ActivationKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ActivationKeyServiceImpl implements ActivationKeyService {

    private final ActivationKeyRepository activationKeyRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;

    public void activateKey(ActivationRequest request) {
        // Lấy user hiện tại từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentEmail = authentication.getName(); // Vì bạn login bằng email

        // Tìm user theo email
        User currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Chỉ cho phép ADMIN gọi API này
        if (!currentUser.getRole().equals(Role.ADMIN)) {
            throw new RuntimeException("Bạn không có quyền thực hiện hành động này");
        }
        ActivationKey activationKey = activationKeyRepository.findByActivationKeyIgnoreCase(request.getKey())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy key"));

        if (activationKey.isActivated()) {
            throw new RuntimeException("Key đã được sử dụng");
        }

        MembershipType type = request.getMembershipType();
        if (type == null) {
            type = activationKey.getMembershipType(); // fallback nếu không truyền loại
        }

        User user = activationKey.getUser();
        user.setRole(Role.EMPLOYER);

        Membership membership = user.getMembership();
        if (membership == null) {
            membership = new Membership();
            membership.setUser(user);
        }

        LocalDate startDate = LocalDate.now();
        membership.setName(user.getFullName());
        membership.setMembershipType(type);
        membership.setPostLimit(type.getPostLimit());
        membership.setMembershipStartDate(startDate);
        membership.setMembershipEndDate(type.getDurationDays() > 0 ? startDate.plusDays(type.getDurationDays()) : null);

        membershipRepository.save(membership);
        user.setMembership(membership);
        userRepository.save(user);

        activationKey.setActivated(true);
        activationKey.setActivatedAt(LocalDateTime.now());
        activationKey.setExpiredAt(type.getDurationDays() > 0 ? LocalDateTime.now().plusDays(type.getDurationDays()) : null);
        activationKey.setMembershipType(type);
        activationKeyRepository.save(activationKey);
    }



    @Override
    public void deactivateKey(String key) {
        ActivationKey activationKey = activationKeyRepository.findByActivationKeyIgnoreCase(key)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy key"));

        if (!activationKey.isActivated()) {
            throw new RuntimeException("Key chưa được kích hoạt");
        }

        // Thu hồi quyền người dùng
        User user = activationKey.getUser();
        user.setRole(Role.USER);

        // Huỷ kích hoạt key
        activationKey.setActivated(false);
        activationKey.setActivatedAt(null);
        activationKey.setExpiredAt(null);
        activationKey.setMembershipType(MembershipType.BASIC);

        // Vô hiệu hóa membership
        Membership membership = user.getMembership();
        if (membership != null) {
            membership.setMembershipStartDate(null);
            membership.setMembershipEndDate(null);
            membership.setPostLimit(0); // Nếu muốn reset post limit
            membership.setMembershipType(MembershipType.BASIC); // Nếu muốn reset loại
            membershipRepository.save(membership);
        }

        user.setMembership(null); // Gỡ liên kết với membership

        // Lưu lại các thay đổi
        userRepository.save(user);
        activationKeyRepository.save(activationKey);
    }

}
