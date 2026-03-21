package com.apt.visitorvehicle.controller;

import com.apt.common.response.ResultResponse;
import com.apt.common.security.UserPrincipal;
import com.apt.visitorvehicle.dto.request.FixedVisitorVehicleReq;
import com.apt.visitorvehicle.service.FixedVisitorVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/visitor-vehicles/fixed")
@RequiredArgsConstructor
public class AdminFixedVisitorVehicleController {
    private final FixedVisitorVehicleService fixedVisitorVehicleService;

    // 관리자 전체 고정 방문차량 목록 조회
    @GetMapping
    public ResultResponse<?> getAdminFixedVisitorVehicles(
            @RequestParam(required = false) String vehicleNumber,
            @RequestParam(required = false) String dong,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResultResponse.success("조회 성공",
                fixedVisitorVehicleService.getAdminFixedVisitorVehicles(vehicleNumber, dong, page, size));
    }

    // 관리자 고정 방문차량 등록
    @PostMapping
    public ResultResponse<?> registerAdminFixedVisitorVehicle(
            @RequestBody FixedVisitorVehicleReq req,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        fixedVisitorVehicleService.adminRegisterFixedVisitorVehicle(req, userPrincipal.getUserId());
        return ResultResponse.success("등록 성공", null);
    }

    // 관리자 고정 방문차량 통계
    @GetMapping("/stats")
    public ResultResponse<?> getAdminFixedVisitorVehicleStats() {
        return ResultResponse.success("조회 성공",
                fixedVisitorVehicleService.getAdminFixedVisitorVehicleStats());
    }

    // 관리자 고정 방문차량 삭제
    @DeleteMapping("/{fixedId}")
    public ResultResponse<?> deleteAdminFixedVisitorVehicle(
            @PathVariable Long fixedId) {
        fixedVisitorVehicleService.adminDeleteFixedVisitorVehicle(fixedId);
        return ResultResponse.success("삭제 성공", null);
    }
}