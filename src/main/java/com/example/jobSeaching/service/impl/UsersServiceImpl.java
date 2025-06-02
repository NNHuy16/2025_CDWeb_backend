package com.example.jobSeaching.service.impl;

import com.example.jobSeaching.dto.LoginRequest;
import com.example.jobSeaching.dto.LoginResponse;
import com.example.jobSeaching.dto.RegisterRequest;
import com.example.jobSeaching.entity.*;
import com.example.jobSeaching.entity.enums.*;
import com.example.jobSeaching.repository.*;
import com.example.jobSeaching.security.JwtTokenProvider;
import com.example.jobSeaching.service.MailService;
import com.example.jobSeaching.service.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@Service
@Transactional
public class UsersServiceImpl implements UsersService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;
    private final EmailChangeRequestRepository emailChangeRequestRepository;
    private final MailService mailService;
    private final ActivationKeyRepository activationKeyRepository;

    public LoginResponse login(LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        String token = tokenProvider.generateToken(auth);
        return new LoginResponse(token, auth.getAuthorities().toString());
    }


    public User registerUserLocal(RegisterRequest registerRequest) {
        if (existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }
        if (existsByPhoneNumber(registerRequest.getPhoneNumber())) {
            throw new IllegalArgumentException("Số điện thoại đã được sử dụng");
        }
        User user = new User();
        user.setFullName(registerRequest.getLastName() + " " + registerRequest.getFirstName());
        user.setEmail(registerRequest.getEmail());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setDateOfBirth(registerRequest.getDateOfBirth());
        user.setLogoUrl(registerRequest.getLogoUrl());
        user.setProvider(AuthProvider.LOCAL);
        user.setRole(Role.USER);

// Gọi lần 1 để có ID
        userRepository.save(user);

// Set keyId
        user.setKeyId("NHK" + user.getId());

// Gọi lần 2 để cập nhật keyId và lấy kết quả sau khi lưu
        User savedUser = userRepository.save(user);

// Tạo ActivationKey liên quan
        ActivationKey activationKey = new ActivationKey();
        activationKey.setUser(savedUser);
        activationKey.setFullName(savedUser.getFullName());
        activationKey.setActivationKey(savedUser.getKeyId());
        activationKey.setMembershipType(MembershipType.BASIC);
        activationKey.setActivated(false);

// Lưu activationKey
        activationKeyRepository.save(activationKey);

        Membership membership = new Membership();
        membership.setMembershipType(MembershipType.BASIC);
        membership.setName(user.getFullName());
        membership.setPostLimit(MembershipType.BASIC.getPostLimit());
        user.setMembership(membership);

        return savedUser;

    }



    public User loginReisterUserGoogle(OAuth2AuthenticationToken auth) {
        OAuth2User oAuth2User = auth.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            if (existingUser.getProvider() != AuthProvider.GOOGLE) {
                // Người dùng đã đăng ký local, không cho login Google
                throw new IllegalArgumentException("Tài khoản đã tồn tại.");
            }

            return existingUser;
        }

        User user = new User();
        user.setEmail(email);
        user.setFullName(name);
        user.setLogoUrl(picture);
        user.setRole(Role.USER);
        user.setProvider(AuthProvider.GOOGLE);

        return userRepository.save(user);
    }

//    public User loginReisterUserFacebook(OAuth2AuthenticationToken auth) {
//        OAuth2User oAuth2User = auth.getPrincipal();
//        // Lấy thông tin user từ Facebook
//        String email = oAuth2User.getAttribute("email");
//        String name = oAuth2User.getAttribute("name");
//        String picture = oAuth2User.getAttribute("picture"); // Có thể khác tùy scope Facebook trả về
//
//        // Kiểm tra user đã tồn tại chưa
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        if (optionalUser.isPresent()) {
//            return optionalUser.get();
//        }
//
//        User user = new User();
//        user.setEmail(email);
//        user.setFullName(name);
//        user.setLogoUrl(picture);
//        user.setRole(Role.USER);
//        user.setProvider(AuthProvider.FACEBOOK);
//
//        return userRepository.save(user);
//    }




    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(Role role) {
        return userRepository.findByRole(role);
    }

    public User updateUserProfile(String email, User newUserData) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        existingUser.setFullName(newUserData.getFullName());
        existingUser.setPhoneNumber(newUserData.getPhoneNumber());
        existingUser.setDateOfBirth(newUserData.getDateOfBirth());
        existingUser.setLogoUrl(newUserData.getLogoUrl());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByPhoneNumber(String phoneNumber) {
        return userRepository.existsByPhoneNumber(phoneNumber);
    }


    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không đúng");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void requestChangeEmail(String currentEmail, String newEmail) {
        if (existsByEmail(newEmail)) {
            throw new IllegalArgumentException("Email mới đã được sử dụng");
        }

        String otp = String.valueOf(new Random().nextInt(899999) + 100000);
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5);

        emailChangeRequestRepository.deleteByCurrentEmail(currentEmail);

        EmailChangeRequest request = new EmailChangeRequest();
        request.setCurrentEmail(currentEmail);
        request.setNewEmail(newEmail);
        request.setOtp(otp);
        request.setExpiresAt(expiresAt);

        emailChangeRequestRepository.save(request);
        mailService.sendOtpEmail(newEmail, otp);
    }


    public void confirmChangeEmail(String currentEmail, String otp) {
        EmailChangeRequest request = emailChangeRequestRepository.findByCurrentEmail(currentEmail)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu đổi email"));

        if (!request.getOtp().equals(otp)) {
            throw new IllegalArgumentException("OTP không hợp lệ");
        }

        if (request.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP đã hết hạn");
        }

        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng"));

        user.setEmail(request.getNewEmail());
        userRepository.save(user);

        emailChangeRequestRepository.deleteByCurrentEmail(currentEmail);
    }

}
