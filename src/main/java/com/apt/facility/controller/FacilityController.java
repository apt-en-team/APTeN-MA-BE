package com.apt.facility.controller;

import com.apt.common.response.ResultResponse;
import com.apt.facility.model.Facility;
import com.apt.facility.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilityController {
    private final FacilityService facilityService;

    // 실제 시설 목록 조회
    @GetMapping
    public ResultResponse<?> getFacilities() {
        return ResultResponse.success("시설 목록 조회 성공", facilityService.getFacilities());
    }

    // 시설 타입 목록 조회
    @GetMapping("/types")
    public ResultResponse<?> getFacilityTypes() {
        return ResultResponse.success("시설 타입 목록 조회 성공", facilityService.getFacilityTypes());
    }
}
