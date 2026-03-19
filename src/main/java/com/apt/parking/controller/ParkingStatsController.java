package com.apt.parking.controller;

import com.apt.common.response.ResultResponse;
import com.apt.parking.dto.request.ParkingStatsReq;
import com.apt.parking.dto.response.ParkingStatsRes;
import com.apt.parking.service.ParkingStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/parking")
@RequiredArgsConstructor
public class ParkingStatsController {

    private final ParkingStatsService parkingStatsService;

    // API-037 | 주차 통계 조회 (GET /api/admin/parking/stats)
    // ?start_date=2026-03-01&end_date=2026-03-19&type=hourly|daily
    @GetMapping("/stats")
    public ResultResponse<ParkingStatsRes> getParkingStats(
            @ModelAttribute ParkingStatsReq req) {

        return ResultResponse.success("주차 통계 조회 성공", parkingStatsService.getParkingStats(req));
    }
}