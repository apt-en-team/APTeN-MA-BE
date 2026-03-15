package com.apt.visitorvehicle.controller;

import com.apt.visitorvehicle.dto.request.AdminVisitorVehicleGetReq;
import com.apt.visitorvehicle.service.VisitorVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/visitor-vehicles")
@RequiredArgsConstructor
public class AdminVisitorVehicleController {
    private final VisitorVehicleService visitorVehicleService;

    // API-069 | 관리자 방문 예정 현황 조회
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAdminVisitorVehicleStatus(
            @ModelAttribute AdminVisitorVehicleGetReq req) {
        Map<String, Object> result = visitorVehicleService.getAdminVisitorVehicles(req);
        return ResponseEntity.ok(result);
    }
}
