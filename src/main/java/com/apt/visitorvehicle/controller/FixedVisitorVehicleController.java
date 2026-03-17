package com.apt.visitorvehicle.controller;

import com.apt.common.response.ResultResponse;
import com.apt.common.security.UserPrincipal;
import com.apt.visitorvehicle.dto.request.FixedVisitorVehicleReq;
import com.apt.visitorvehicle.service.FixedVisitorVehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visitor-vehicles/fixed")
@RequiredArgsConstructor
public class FixedVisitorVehicleController {
    private final FixedVisitorVehicleService fixedVisitorVehicleService;

    // API-062 | 고정 방문차량 등록
    @PostMapping
    public ResultResponse<?> registerFixedVisitorVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody FixedVisitorVehicleReq req) {
        return ResultResponse.success("등록 성공",
                fixedVisitorVehicleService.registerVisitorVehicle(userPrincipal.getUserId(), req));
    }

    // API-063 | 내 고정 방문차량 목록 조회
    @GetMapping("/my")
    public ResultResponse<?> getMyFixedVisitorVehicles(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) String vehicleNumber,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResultResponse.success("조회 성공",
                fixedVisitorVehicleService.getMyFixedVisitorVehicles(userPrincipal.getUserId(), vehicleNumber, page, size));
    }

    // API-065 | 고정 방문차량 삭제
    @DeleteMapping("/{fixedId}")
    public ResultResponse<?> deleteFixedVisitorVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long fixedId) {
        fixedVisitorVehicleService.deleteFixedVisitorVehicle(userPrincipal.getUserId(), fixedId);
        return ResultResponse.success("삭제 성공", null);
    }
}