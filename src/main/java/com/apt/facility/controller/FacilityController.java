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

    @GetMapping
    public ResultResponse<?> getFacilities() {
        return ResultResponse.success("시설 목록 조회 성공", facilityService.getFacilities());
    }
}
