package com.example.jobSeaching.entity.Cron;

import com.example.jobSeaching.entity.User;
import com.example.jobSeaching.entity.VerificationToken;
import com.example.jobSeaching.repository.UserRepository;
import com.example.jobSeaching.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TokenCleanupService {

    @Autowired
    private VerificationTokenRepository tokenRepo;

    @Autowired
    private UserRepository userRepo;

    @Scheduled(fixedRate = 300000) // chạy mỗi 5 phút
    public void cleanUpExpiredTokens() {
        System.out.println("🔁 Cron chạy lúc: " + LocalDateTime.now());
        List<VerificationToken> expiredTokens = tokenRepo.findAllExpired(LocalDateTime.now());
        System.out.println("🔍 Số token hết hạn: " + expiredTokens.size());
        for (VerificationToken token : expiredTokens) {
            User user = token.getUser();
            if (!user.isEnabled()) {
                tokenRepo.delete(token);
                userRepo.delete(user);
            }
        }
    }
}
