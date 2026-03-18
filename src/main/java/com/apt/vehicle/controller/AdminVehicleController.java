package com.apt.vehicle.controller;

import com.apt.common.response.ResultResponse;
import com.apt.common.security.UserPrincipal;
import com.apt.vehicle.dto.request.VehicleAdminRegisterReq;
import com.apt.vehicle.dto.request.VehicleAdminSearchReq;
import com.apt.vehicle.dto.response.VehicleAdminRes;
import com.apt.vehicle.dto.response.VehiclePageRes;
import com.apt.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 차량 관리 Controller | ADMIN */
@RestController
@RequiredArgsConstructor
public class AdminVehicleController {

    private final VehicleService vehicleService;

    /** API-042 | 전체 차량 목록 조회 */
    @GetMapping("/api/admin/vehicles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<VehiclePageRes<VehicleAdminRes>>> getAllVehicles(
            @ModelAttribute VehicleAdminSearchReq req) {
        return ResponseEntity.ok(ResultResponse.success("전체 차량 목록 조회 성공",
                vehicleService.getAllVehicles(req)));
    }

    /** ADMIN | 차량 통계 조회 */
    @GetMapping("/api/admin/vehicles/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<Map<String, Long>>> getVehicleStats() {
        return ResponseEntity.ok(ResultResponse.success("차량 통계 조회 성공",
                vehicleService.getVehicleStats()));
    }

    /** ADMIN | 차량 등록 */
    @PostMapping("/api/admin/vehicles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<?>> adminRegisterVehicle(
            @RequestBody @Valid VehicleAdminRegisterReq req) {
        return ResponseEntity.ok(ResultResponse.success("차량 등록 성공",
                vehicleService.adminRegisterVehicle(req)));
    }

    /** ADMIN | 동 목록 조회 */
    @GetMapping("/api/admin/vehicles/dongs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<List<String>>> getDongs() {
        return ResponseEntity.ok(ResultResponse.success("동 목록 조회 성공",
                vehicleService.getDongs()));
    }

    /** ADMIN | 차량 승인 */
    @PatchMapping("/api/admin/vehicles/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<Void>> approveVehicle(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        vehicleService.approveVehicle(id, userPrincipal.getUserId());
        return ResponseEntity.ok(ResultResponse.success("차량 승인 완료", null));
    }

    /** ADMIN | 차량 거부 */
    @PatchMapping("/api/admin/vehicles/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<Void>> rejectVehicle(
            @PathVariable Long id) {
        vehicleService.rejectVehicle(id);
        return ResponseEntity.ok(ResultResponse.success("차량 거부 완료", null));
    }

    /** ADMIN | 차량번호 중복 확인 */
    @GetMapping("/api/admin/vehicles/check-plate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<Boolean>> checkLicensePlate(
            @RequestParam String licensePlate) {
        boolean isDuplicate = vehicleService.existsByLicensePlate(licensePlate);
        return ResponseEntity.ok(ResultResponse.success("중복 확인 완료", isDuplicate));
    }
}