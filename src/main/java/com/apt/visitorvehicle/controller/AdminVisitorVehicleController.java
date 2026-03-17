package com.apt.visitorvehicle.controller;

import com.apt.common.response.ResultResponse;
import com.apt.visitorvehicle.dto.request.AdminVisitorVehicleGetReq;
import com.apt.visitorvehicle.service.VisitorVehicleService;
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
}