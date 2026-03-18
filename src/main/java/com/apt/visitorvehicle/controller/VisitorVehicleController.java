package com.apt.visitorvehicle.controller;

import com.apt.common.response.ResultResponse;
import com.apt.common.security.UserPrincipal;
import com.apt.visitorvehicle.dto.request.VisitorVehicleGetReq;
import com.apt.visitorvehicle.dto.request.VisitorVehicleReq;
import com.apt.visitorvehicle.dto.request.VisitorVehicleReuseReq;
import com.apt.visitorvehicle.service.VisitorVehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visitor-vehicles")
@RequiredArgsConstructor
public class VisitorVehicleController {
    private final VisitorVehicleService visitorVehicleService;

    // API-030 | 방문차량 사전등록
    @PostMapping
    public ResultResponse<?> registerVisitorVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody VisitorVehicleReq req) {
        return ResultResponse.success("등록 성공",
                visitorVehicleService.registerVisitorVehicle(userPrincipal.getUserId(), req));
    }

    // API-031 | 내 방문차량 목록 조회
    @GetMapping("/my")
    public ResultResponse<?> getMyVisitorVehicles(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @ModelAttribute VisitorVehicleGetReq req) {
        return ResultResponse.success("조회 성공",
                visitorVehicleService.getMyVisitorVehicles(userPrincipal.getUserId(), req));
    }

    // API-032 | 방문차량 상세 조회
    @GetMapping("/{id}")
    public ResultResponse<?> getVisitorVehicleDetail(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("id") Long visitorVehicleId) {
        return ResultResponse.success("조회 성공",
                visitorVehicleService.getVisitorVehicleDetail(userPrincipal.getUserId(), visitorVehicleId));
    }

    // API-033 | 방문차량 재사용 등록
    @PostMapping("/{id}/reuse")
    public ResultResponse<?> reuseVisitorVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("id") Long visitorVehicleId,
            @Valid @RequestBody VisitorVehicleReuseReq req) {
        return ResultResponse.success("재등록 성공",
                visitorVehicleService.reuseVisitorVehicle(userPrincipal.getUserId(), visitorVehicleId, req.getVisitDate()));
    }

    // API-034 | 방문차량 수정
    @PutMapping("/{id}")
    public ResultResponse<?> updateVisitorVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("id") Long visitorVehicleId,
            @Valid @RequestBody VisitorVehicleReq req) {
        visitorVehicleService.updateVisitorVehicle(userPrincipal.getUserId(), visitorVehicleId, req);
        return ResultResponse.success("수정 성공", null);
    }

    // API-035 | 방문차량 삭제
    @DeleteMapping("/{id}")
    public ResultResponse<?> deleteVisitorVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("id") Long visitorVehicleId) {
        visitorVehicleService.deleteVisitorVehicle(userPrincipal.getUserId(), visitorVehicleId);
        return ResultResponse.success("삭제 성공", null);
    }

    // API-036 | 등록 취소
    @PatchMapping("/{id}/cancel")
    public ResultResponse<?> cancelVisitorVehicle(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("id") Long visitorVehicleId) {
        visitorVehicleService.cancelVisitorVehicle(userPrincipal.getUserId(), visitorVehicleId);
        return ResultResponse.success("취소 성공", null);
    }
}