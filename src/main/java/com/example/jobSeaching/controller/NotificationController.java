package com.example.jobSeaching.controller;

import com.example.jobSeaching.entity.AdminNotification;
import com.example.jobSeaching.repository.AdminNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notifications")
public class NotificationController {

    @Autowired
    private AdminNotificationRepository notificationRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<AdminNotification> getAllNotifications() {
        return notificationRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/mark-read/{id}")
    public ResponseEntity<?> markAsRead(@PathVariable Long id) {
        AdminNotification noti = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thông báo không tồn tại"));
        noti.setRead(true);
        notificationRepository.save(noti);
        return ResponseEntity.ok("Đã đánh dấu là đã đọc");
    }
}
