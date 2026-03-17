package com.apt.visitorvehicle.controller;

import com.apt.common.response.ResultResponse;
import com.apt.visitorvehicle.service.FixedVisitorVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/visitor-vehicles/fixed")
@RequiredArgsConstructor
public class AdminFixedVisitorVehicleController {
    private final FixedVisitorVehicleService fixedVisitorVehicleService;

    // API-071 | 관리자 전체 고정 방문차량 목록 조회
    @GetMapping
    public ResultResponse<?> getAdminFixedVisitorVehicles(
            @RequestParam(required = false) String vehicleNumber,
            @RequestParam(required = false) String dong,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResultResponse.success("조회 성공",
                fixedVisitorVehicleService.getAdminFixedVisitorVehicles(vehicleNumber, dong, page, size));
    }

    // API-072 | 관리자 고정 방문차량 통계
    @GetMapping("/stats")
    public ResultResponse<?> getAdminFixedVisitorVehicleStats() {
        return ResultResponse.success("조회 성공",
                fixedVisitorVehicleService.getAdminFixedVisitorVehicleStats());
    }
}