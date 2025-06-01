package com.example.jobSeaching.service;

import com.example.jobSeaching.entity.ActivationKey;
import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.enums.Role;
import com.example.jobSeaching.repository.ActivationKeyRepository;
import com.example.jobSeaching.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActivationKeyService {

    private final ActivationKeyRepository activationKeyRepository;
    private final UserRepository userRepository;

    public ActivationKeyService(ActivationKeyRepository activationKeyRepository, UserRepository userRepository) {
        this.activationKeyRepository = activationKeyRepository;
        this.userRepository = userRepository;
    }



    public void activateKey(String key) {
        ActivationKey activationKey = activationKeyRepository.findByActivationKeyIgnoreCase(key)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy key"));

        if (activationKey.isActivated()) {
            throw new RuntimeException("Key đã được sử dụng");
        }

        if (activationKey.getExpiredAt() != null && activationKey.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Key đã hết hạn");
        }

        User user = activationKey.getUser();
        user.setRole(Role.EMPLOYER);
        userRepository.save(user);

        activationKey.setActivated(true);
        activationKeyRepository.save(activationKey);
    }

    public void deactivateKey(String key) {
        ActivationKey activationKey = activationKeyRepository.findByActivationKeyIgnoreCase(key)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy key"));

        if (!activationKey.isActivated()) {
            throw new RuntimeException("Key chưa được kích hoạt");
        }

        activationKey.setActivated(false);
        activationKeyRepository.save(activationKey);

        User user = activationKey.getUser();
        user.setRole(Role.USER);
        userRepository.save(user);
    }
}