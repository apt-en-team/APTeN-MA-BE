package com.apt.facility.controller;

import com.apt.common.response.ResultResponse;
import com.apt.facility.model.Facility;
import com.apt.facility.dto.response.FacilityRes;
import com.apt.facility.dto.response.FacilityTypeRes;
import com.apt.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 시설 Controller | RESIDENT */
@RestController
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    /** API-044 | 시설 타입 목록 조회 */
    @GetMapping("/api/admin/facility-types")
    public ResponseEntity<ResultResponse<List<FacilityTypeRes>>> getAllTypes() {
        return ResponseEntity.ok(ResultResponse.success("시설 타입 목록 조회 성공",
                facilityService.getAllTypes()));
    }

    /** API-048 | 시설 목록 조회 */
    @GetMapping("/api/facilities")
    public ResponseEntity<ResultResponse<List<FacilityRes>>> getAllFacilities(
            @RequestParam(required = false) Long typeId) {
        return ResponseEntity.ok(ResultResponse.success("시설 목록 조회 성공",
                facilityService.getAllFacilities(typeId)));
    }

    /** API-049 | 시설 상세 조회 */
    @GetMapping("/api/facilities/{id}")
    public ResponseEntity<ResultResponse<FacilityRes>> getFacility(
            @PathVariable Long id) {
        return ResponseEntity.ok(ResultResponse.success("시설 상세 조회 성공",
                facilityService.getFacility(id)));
    }

    // 실제 시설 목록 조회
    @GetMapping("/api/facilities")
    public ResultResponse<?> getFacilities() {
        return ResultResponse.success("시설 목록 조회 성공", facilityService.getFacilities());
    }

    // 시설 타입 목록 조회
    @GetMapping("/api/facilities/types")
    public ResultResponse<?> getFacilityTypes() {
        return ResultResponse.success("시설 타입 목록 조회 성공", facilityService.getFacilityTypes());
    }
}