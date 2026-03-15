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

    /*
     * API-069 | 관리자 방문 예정 현황 조회
     * GET /api/admin/visitor-vehicles
     * - 날짜(visit_date), 차량번호(license_plate) 필터 + 페이징
     * - 등록자 이름, 동/호 정보 포함 (user/household JOIN)
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAdminVisitorVehicleStatus(
            @ModelAttribute AdminVisitorVehicleGetReq req) {
        Map<String, Object> result = visitorVehicleService.getAdminVisitorVehicles(req);
        return ResponseEntity.ok(result);
    }

    /*
     * API-070 | 관리자 방문차량 통계 조회
     * GET /api/admin/visitor-vehicles/stats
     * - 오늘 방문 예정 / 내일 방문 예정 / 이번 달 방문 / 전체 등록 건수 반환
     * - Java LocalDate.now() 기준으로 날짜 계산 (UTC 오차 방지)
     */
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getAdminVisitorVehicleStats() {
        Map<String, Object> result = visitorVehicleService.getAdminVisitorVehicleStats();
        return ResponseEntity.ok(result);
    }
}