package com.apt.visitorvehicle.controller;

import com.apt.common.response.ResultResponse;
import com.apt.visitorvehicle.dto.request.AdminVisitorVehicleGetReq;
import com.apt.visitorvehicle.dto.request.AdminVisitorVehicleReq;
import com.apt.visitorvehicle.service.VisitorVehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/visitor-vehicles")
@RequiredArgsConstructor
public class AdminVisitorVehicleController {

    private final VisitorVehicleService visitorVehicleService;

    // API-069 | 관리자 방문 예정 현황 조회
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResultResponse<?> getAdminVisitorVehicleStatus(
            @ModelAttribute AdminVisitorVehicleGetReq req) {
        return ResultResponse.success("조회 성공",
                visitorVehicleService.getAdminVisitorVehicles(req));
    }

    // API-070 | 관리자 방문차량 통계 조회
    @GetMapping("/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResultResponse<?> getAdminVisitorVehicleStats() {
        return ResultResponse.success("조회 성공",
                visitorVehicleService.getAdminVisitorVehicleStats());
    }

    // API-071 | 관리자 방문차량 등록
    // 관리자가 입주민 대신 방문차량 등록 (userId를 body로 직접 받음)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResultResponse<?> registerVisitorVehicleByAdmin(
            @Valid @RequestBody AdminVisitorVehicleReq req) {
        return ResultResponse.success("등록 성공",
                visitorVehicleService.registerVisitorVehicle(req.getUserId(), toVisitorVehicleReq(req)));
    }

    // AdminVisitorVehicleReq → VisitorVehicleReq 변환 (기존 서비스 재사용)
    private com.apt.visitorvehicle.dto.request.VisitorVehicleReq toVisitorVehicleReq(AdminVisitorVehicleReq req) {
        com.apt.visitorvehicle.dto.request.VisitorVehicleReq vReq = new com.apt.visitorvehicle.dto.request.VisitorVehicleReq();
        vReq.setLicensePlate(req.getLicensePlate());
        vReq.setVisitorName(req.getVisitorName());
        vReq.setVisitPurpose(req.getVisitPurpose());
        vReq.setVisitDate(req.getVisitDate());
        return vReq;
    }
}