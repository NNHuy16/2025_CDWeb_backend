package com.example.jobSeaching.service;

import com.example.jobSeaching.dto.LoginRequest;
import com.example.jobSeaching.dto.LoginResponse;
import com.example.jobSeaching.dto.RegisterRequest;
import com.example.jobSeaching.entity.enums.AuthProvider;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.repository.UserRepository;
import com.example.jobSeaching.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UsersService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtTokenProvider tokenProvider;

    public UsersService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authManager, JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.tokenProvider = tokenProvider;
    }

    public User createUsers(User user) {
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        else if (user.getRole() == Role.EMPLOYER) {
            user.setRole(Role.EMPLOYER);
        }
        else{user.setRole(Role.ADMIN);}
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

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
        user.setLogoUrl(registerRequest.getLogoUrl());
        user.setProvider(AuthProvider.LOCAL);
        user.setRole(Role.USER);
        return userRepository.save(user);
    }


    public User loginReisterUserGoogle(OAuth2AuthenticationToken auth) {
        OAuth2User oAuth2User = auth.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = new User();
        user.setEmail(email);
        user.setFullName(name);
        user.setLogoUrl(picture);
        user.setRole(Role.USER);
        user.setProvider(AuthProvider.GOOGLE);

        return userRepository.save(user);
    }

    public User loginReisterUserFacebook(OAuth2AuthenticationToken auth) {
        OAuth2User oAuth2User = auth.getPrincipal();
        // Lấy thông tin user từ Facebook
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture"); // Có thể khác tùy scope Facebook trả về

        // Kiểm tra user đã tồn tại chưa
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = new User();
        user.setEmail(email);
        user.setFullName(name);
        user.setLogoUrl(picture);
        user.setRole(Role.USER);
        user.setProvider(AuthProvider.FACEBOOK);

        return userRepository.save(user);
    }




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

    public User updateUser(User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(user);
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

}





