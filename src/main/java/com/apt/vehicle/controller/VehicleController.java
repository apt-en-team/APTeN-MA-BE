package com.apt.vehicle.controller;

import com.apt.common.response.ResultResponse;
import com.apt.common.security.JwtUser;
import com.apt.vehicle.dto.request.VehicleReq;
import com.apt.vehicle.dto.request.VehicleUpdateReq;
import com.apt.vehicle.dto.response.VehicleLogRes;
import com.apt.vehicle.dto.response.VehicleRes;
import com.apt.vehicle.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 차량 Controller | RESIDENT */
@RestController
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    /** API-038 | 내 차량 목록 조회 */
    @GetMapping("/api/vehicles/my")
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<ResultResponse<List<VehicleRes>>> getMyVehicles(
            @AuthenticationPrincipal JwtUser jwtUser) {
        return ResponseEntity.ok(ResultResponse.success("내 차량 목록 조회 성공",
                vehicleService.getMyVehicles(jwtUser.getUserId())));
    }

    /** API-039 | 차량 등록 */
    @PostMapping("/api/vehicles")
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<ResultResponse<VehicleRes>> registerVehicle(
            @RequestBody @Valid VehicleReq req,
            @AuthenticationPrincipal JwtUser jwtUser) {
        return ResponseEntity.ok(ResultResponse.success("차량 등록 성공",
                vehicleService.registerVehicle(req, jwtUser.getUserId())));
    }

    /** API-040 | 차량 수정 */
    @PutMapping("/api/vehicles/{id}")
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<ResultResponse<VehicleRes>> updateVehicle(
            @PathVariable Long id,
            @RequestBody VehicleUpdateReq req,
            @AuthenticationPrincipal JwtUser jwtUser) {
        return ResponseEntity.ok(ResultResponse.success("차량 수정 성공",
                vehicleService.updateVehicle(id, req, jwtUser.getUserId())));
    }

    /** API-041 | 차량 삭제 */
    @DeleteMapping("/api/vehicles/{id}")
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<ResultResponse<Void>> deleteVehicle(
            @PathVariable Long id,
            @AuthenticationPrincipal JwtUser jwtUser) {
        vehicleService.deleteVehicle(id, jwtUser.getUserId());
        return ResponseEntity.ok(ResultResponse.success("차량 삭제 성공", null));
    }

    /** API-043 | 내 차량 입출차 기록 조회 */
    @GetMapping("/api/vehicles/my-logs")
    @PreAuthorize("hasRole('RESIDENT')")
    public ResponseEntity<ResultResponse<List<VehicleLogRes>>> getMyVehicleLogs(
            @AuthenticationPrincipal JwtUser jwtUser) {
        return ResponseEntity.ok(ResultResponse.success("입출차 기록 조회 성공",
                vehicleService.getMyVehicleLogs(jwtUser.getUserId())));
    }
}