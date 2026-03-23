package com.apt.facility.controller;

import com.apt.common.response.ResultResponse;
import com.apt.facility.dto.request.FacilityReq;
import com.apt.facility.dto.request.FacilityTypeReq;
import com.apt.facility.dto.response.FacilityRes;
import com.apt.facility.dto.response.FacilityTypeRes;
import com.apt.facility.service.FacilityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 시설 관리 Controller | ADMIN */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminFacilityController {

    private final FacilityService facilityService;

    /** API-045 | 시설 타입 등록 */
    @PostMapping("/facility-types")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<FacilityTypeRes>> createType(
            @RequestBody @Valid FacilityTypeReq req) {
        return ResponseEntity.ok(ResultResponse.success("시설 타입 등록 성공",
                facilityService.createType(req)));
    }

    /** API-046 | 시설 타입 수정 */
    @PutMapping("/facility-types/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<FacilityTypeRes>> updateType(
            @PathVariable Long id,
            @RequestBody @Valid FacilityTypeReq req) {
        return ResponseEntity.ok(ResultResponse.success("시설 타입 수정 성공",
                facilityService.updateType(id, req)));
    }

    /** API-047 | 시설 타입 삭제 */
    @DeleteMapping("/facility-types/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<Void>> deleteType(
            @PathVariable Long id) {
        facilityService.deleteType(id);
        return ResponseEntity.ok(ResultResponse.success("시설 타입 삭제 성공", null));
    }

    /** API-050 | 시설 등록 */
    @PostMapping("/facilities")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<FacilityRes>> createFacility(
            @RequestBody @Valid FacilityReq req) {
        return ResponseEntity.ok(ResultResponse.success("시설 등록 성공",
                facilityService.createFacility(req)));
    }

    /** API-051 | 시설 수정 */
    @PutMapping("/facilities/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<FacilityRes>> updateFacility(
            @PathVariable Long id,
            @RequestBody @Valid FacilityReq req) {
        return ResponseEntity.ok(ResultResponse.success("시설 수정 성공",
                facilityService.updateFacility(id, req)));
    }

    /** API-052 | 시설 삭제 */
    @DeleteMapping("/facilities/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResultResponse<Void>> deleteFacility(
            @PathVariable Long id) {
        facilityService.deleteFacility(id);
        return ResponseEntity.ok(ResultResponse.success("시설 삭제 성공", null));
    }

    /** API-044 | 시설 타입 목록 조회 */
    @GetMapping("/facility-types")
    public ResponseEntity<ResultResponse<List<FacilityTypeRes>>> getAllTypes() {
        return ResponseEntity.ok(ResultResponse.success("시설 타입 목록 조회 성공",
                facilityService.getAllTypes()));
    }


}