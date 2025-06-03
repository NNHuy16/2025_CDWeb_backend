package com.example.jobSeaching.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationSender {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendToAdmin(String message) {
        messagingTemplate.convertAndSend("/topic/admin", message); // client sub tại đây
    }
}
