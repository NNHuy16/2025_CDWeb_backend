package com.example.jobSeaching.controller;

import com.example.jobSeaching.dto.UpgradeRequest;
import com.example.jobSeaching.service.UpgradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// controller/UpgradeController.java
@RestController
@RequestMapping("/api/payment")
public class UpgradeController {

    @Autowired
    private UpgradeService upgradeService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/upgrade")
    public ResponseEntity<String> upgrade(@RequestBody UpgradeRequest request) {
        upgradeService.upgradeMembership(request);
        return ResponseEntity.ok("Nâng cấp thành công");
    }
}
