package com.apt.vehicle.controller;

import com.apt.common.response.ResultResponse;
import com.apt.common.security.JwtUser;
import com.apt.vehicle.dto.request.VehicleAdminSearchReq;
import com.apt.vehicle.dto.request.VehicleReq;
import com.apt.vehicle.dto.request.VehicleUpdateReq;
import com.apt.vehicle.dto.response.VehicleAdminRes;
import com.apt.vehicle.dto.response.VehicleLogRes;
import com.apt.vehicle.dto.response.VehiclePageRes;
import com.apt.vehicle.dto.response.VehicleRes;
import com.apt.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 차량 관리 Controller */
@RestController
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    /** API-038 | 내 차량 목록 조회 | RESIDENT */
    @GetMapping("/api/vehicles/my")
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<ResultResponse<List<VehicleRes>>> getMyVehicles(
            @AuthenticationPrincipal JwtUser jwtUser) {
        List<VehicleRes> result = vehicleService.getMyVehicles(jwtUser.getUserId());
        return ResponseEntity.ok(ResultResponse.success("내 차량 목록 조회 성공", result));
    }

    /** API-039 | 차량 등록 | RESIDENT */
    @PostMapping("/api/vehicles")
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<ResultResponse<VehicleRes>> registerVehicle(
            @RequestBody @Valid VehicleReq req,
            @AuthenticationPrincipal JwtUser jwtUser) {
        VehicleRes result = vehicleService.registerVehicle(req, jwtUser.getUserId());
        return ResponseEntity.ok(ResultResponse.success("차량 등록 성공", result));
    }

    /** API-040 | 차량 수정 | RESIDENT */
    @PutMapping("/api/vehicles/{id}")
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<ResultResponse<VehicleRes>> updateVehicle(
            @PathVariable Long id,
            @RequestBody VehicleUpdateReq req,
            @AuthenticationPrincipal JwtUser jwtUser) {
        VehicleRes result = vehicleService.updateVehicle(id, req, jwtUser.getUserId());
        return ResponseEntity.ok(ResultResponse.success("차량 수정 성공", result));
    }

    /** API-041 | 차량 삭제 | RESIDENT */
    @DeleteMapping("/api/vehicles/{id}")
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<ResultResponse<Void>> deleteVehicle(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUser jwtUser) {
        vehicleService.deleteVehicle(id, jwtUser.getUserId());
        return ResponseEntity.ok(ResultResponse.success("차량 삭제 성공", null));
    }

    /** API-043 | 내 차량 입출차 기록 조회 | RESIDENT */
    @GetMapping("/api/vehicles/my-logs")
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<ResultResponse<List<VehicleLogRes>>> getMyVehicleLogs(
            @AuthenticationPrincipal JwtUser jwtUser) {
        List<VehicleLogRes> result = vehicleService.getMyVehicleLogs(jwtUser.getUserId());
        return ResponseEntity.ok(ResultResponse.success("입출차 기록 조회 성공", result));
    }

    /** API-042 | 전체 차량 목록 조회 | ADMIN */
    @GetMapping("/api/admin/vehicles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<VehiclePageRes<VehicleAdminRes>>> getAllVehicles(
            @ModelAttribute VehicleAdminSearchReq req) {
        VehiclePageRes<VehicleAdminRes> result = vehicleService.getAllVehicles(req);
        return ResponseEntity.ok(ResultResponse.success("전체 차량 목록 조회 성공", result));
    }

    /** ADMIN | 차량 통계 조회  */
    @GetMapping("/api/admin/vehicles/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<Map<String, Long>>> getVehicleStats() {
        return ResponseEntity.ok(ResultResponse.success("차량 통계 조회 성공", vehicleService.getVehicleStats()));
    }

    /** ADMIN | 차량 승인 */
    @PatchMapping("/api/admin/vehicles/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<Void>> approveVehicle(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUser jwtUser) {
        vehicleService.approveVehicle(id, jwtUser.getUserId());
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

    /** ADMIN | 동 목록 조회 */
    @GetMapping("/api/admin/vehicles/dongs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<List<String>>> getDongs() {
        return ResponseEntity.ok(ResultResponse.success("동 목록 조회 성공", vehicleService.getDongs()));
    }
}