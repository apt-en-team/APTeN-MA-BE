package com.apt.facility.controller;

import com.apt.common.response.ResultResponse;
import com.apt.facility.dto.response.FacilityRes;
import com.apt.facility.dto.response.FacilityTypeRes;
import com.apt.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 시설 Controller | 입주민/공통 API
 * 담당자: 손지혜
 */
@RestController
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    /**
     * API-044 | 시설 타입 목록 조회 (입주민)
     * GET /api/resident/facility-types
     */
    @GetMapping("/api/resident/facility-types")
    public ResponseEntity<ResultResponse<List<FacilityTypeRes>>> getTypesForResident() {
        return ResponseEntity.ok(ResultResponse.success("시설 타입 목록 조회 성공",
                facilityService.getAllTypes()));
    }

    /**
     * API-048 | 시설 목록 조회 (입주민, 타입 필터)
     * GET /api/resident/facilities?typeId={typeId}
     */
    @GetMapping("/api/resident/facilities")
    public ResponseEntity<ResultResponse<List<FacilityRes>>> getAllFacilities(
            @RequestParam(required = false) Long typeId) {
        return ResponseEntity.ok(ResultResponse.success("시설 목록 조회 성공",
                facilityService.getAllFacilities(typeId)));
    }

    /**
     * API-049 | 시설 상세 조회 (입주민)
     * GET /api/resident/facilities/{id}
     */
    @GetMapping("/api/resident/facilities/{id}")
    public ResponseEntity<ResultResponse<FacilityRes>> getFacility(
            @PathVariable Long id) {
        return ResponseEntity.ok(ResultResponse.success("시설 상세 조회 성공",
                facilityService.getFacility(id)));
    }
}